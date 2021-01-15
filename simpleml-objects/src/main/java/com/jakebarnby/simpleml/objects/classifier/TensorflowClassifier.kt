/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.jakebarnby.simpleml.objects.classifier

import android.app.Activity
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import com.jakebarnby.simpleml.*
import com.jakebarnby.simpleml.classification.ClassifiedResult
import com.jakebarnby.simpleml.classification.Classifier
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.helpers.FileDownloader
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.tensorflow.lite.Interpreter
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.*

/**
 * Classifies images with Tensorflow Lite.
 */

class TensorflowClassifier @Throws(IOException::class) internal constructor(
    private val activity: Activity,
    private val configuration: Classification.Configuration,
    private val fileDownloader: FileDownloader
) : Classifier, CoroutineBase {

    override val job = Job()

    // Preallocated buffers for storing image data in
    private val intValues = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)

    private lateinit var tflite: Interpreter
    private lateinit var labelList: List<String>
    private lateinit var imgData: ByteBuffer
    private lateinit var labelProbArray: Array<FloatArray>

    private lateinit var filterLabelProbArray: Array<FloatArray>

    private var modelPath = "graph.lite"
    private var labelPath = "labels.txt"
    private var remoteModel = false
    private var remoteLabels = false

    private val sortedLabels = PriorityQueue<Map.Entry<String, Float>>(RESULTS_TO_SHOW) { o1, o2 ->
        o1.value.compareTo(o2.value)
    }

    companion object {
        internal const val DIM_IMG_SIZE_X = 224
        internal const val DIM_IMG_SIZE_Y = 224

        private const val TAG = "TfLite"
        private const val RESULTS_TO_SHOW = 5

        // Dimensions of inputs
        private const val DIM_BATCH_SIZE = 1
        private const val DIM_PIXEL_SIZE = 3
        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128.0f
        private const val FILTER_STAGES = 3
        private const val FILTER_FACTOR = 0.4f
    }

    override suspend fun initialise() {
        launch {
            listOf(
                initModelAsync {},
                initLabelsAsync {}
            ).awaitAll()

            listOf(async(IO) {
                val modelFile = loadModelFileAsync(activity).await() ?: return@async
                tflite = Interpreter(modelFile)
            }, async(IO) {
                labelList = loadLabelListAsync(activity).await()
            }).awaitAll()

            imgData = ByteBuffer.allocateDirect(
                4 * DIM_BATCH_SIZE
                        * DIM_IMG_SIZE_X
                        * DIM_IMG_SIZE_Y
                        * DIM_PIXEL_SIZE
            )
            imgData.order(ByteOrder.nativeOrder())
            labelProbArray = Array(1) { FloatArray(labelList.size) }
            filterLabelProbArray = Array(FILTER_STAGES) { FloatArray(labelList.size) }
        }
    }

    private fun initModelAsync(
        onProgress: (Int) -> Unit
    ) = async(IO) {
        if (configuration.modelUrl != null
            && !configuration.modelUrl!!.contains("http")
        ) {
            modelPath = configuration.modelUrl!!
            return@async
        }
        remoteModel = true
        fileDownloader.fetchAsync(
            configuration.modelUrl!!,
            activity.filesDir.absolutePath,
            "graph.lite",
            onProgress
        ).await()
    }

    private fun initLabelsAsync(
        onProgress: (Int) -> Unit
    ) = async(IO) {
        if (configuration.labelUrl != null
            && !configuration.labelUrl!!.contains("http")
        ) {
            labelPath = configuration.labelUrl!!
            return@async
        }
        remoteLabels = true
        fileDownloader.fetchAsync(
            configuration.labelUrl!!,
            activity.filesDir.absolutePath,
            "labels.txt",
            onProgress
        ).await()
    }

    override suspend fun classify(
        album: Collection<Bitmap>,
        onNextClassificationResult: (Collection<ClassifiedResult>) -> Unit
    ) {
        album.map { classifyFrameAsync(it) }.awaitAll()
    }


    override suspend fun classify(bitmap: Bitmap): Collection<ClassifiedResult> =
        classifyFrameAsync(bitmap).await()

    override fun close() {
        tflite.close()
    }

    /**
     * Classifies a frame from the preview stream.
     */
    private fun classifyFrameAsync(bitmap: Bitmap) = async {
        convertBitmapToByteBuffer(bitmap)

        val startTime = SystemClock.uptimeMillis()
        tflite.run(imgData, labelProbArray)
        val endTime = SystemClock.uptimeMillis()
        Log.d(TAG, "Timecost to run model inference: " + (endTime - startTime).toString())

        applyFilter()
        return@async fetchResults()
    }

    private fun applyFilter() {
        val numLabels = labelList.size

        // Low pass filter `labelProbArray` into the first stage of the filter.
        for (j in 0 until numLabels) {
            filterLabelProbArray[0][j] += FILTER_FACTOR * (labelProbArray[0][j] - filterLabelProbArray[0][j])
        }
        // Low pass filter each stage into the next.
        for (i in 1 until FILTER_STAGES) {
            for (j in 0 until numLabels) {
                filterLabelProbArray[i][j] += FILTER_FACTOR * (filterLabelProbArray[i - 1][j] - filterLabelProbArray[i][j])
            }
        }
        // Copy the last stage filter output back to `labelProbArray`.
        System.arraycopy(
            filterLabelProbArray[FILTER_STAGES - 1],
            0,
            labelProbArray[0],
            0,
            numLabels
        )
    }

    /**
     * Reads label list from Assets.
     */
    @Throws(IOException::class)
    private fun loadLabelListAsync(activity: Activity) = async(IO) {
        val labelList = mutableListOf<String>()

        if (remoteLabels) {
            val file = File("${activity.filesDir.absoluteFile}/labels.txt")
            if (file.exists()) {
                file.forEachLine {
                    labelList.add(it)
                }
            }
        } else {
            val reader = BufferedReader(InputStreamReader(activity.assets.open(labelPath)))
            reader.forEachLine {
                labelList.add(it)
            }
            reader.close()
        }
        return@async labelList
    }

    /**
     * Memory-map the model file in Assets.
     */
    @Throws(IOException::class)
    private fun loadModelFileAsync(activity: Activity) = async(IO) {
        val inputStream: FileInputStream
        val startOffset: Long
        val declaredLength: Long

        if (remoteModel) {
            val file = File("${activity.filesDir.absoluteFile}/graph.lite")
            if (file.exists()) {
                inputStream = FileInputStream(file)
                startOffset = 0
                declaredLength = file.length()
            } else {
                throw IOException("Model file does not exist.")
            }
        } else {
            val fileDescriptor = activity.assets.openFd(modelPath)
            inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            startOffset = fileDescriptor.startOffset
            declaredLength = fileDescriptor.declaredLength
        }

        val fileChannel = inputStream.channel

        return@async fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Writes Image data into a [ByteBuffer].
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        imgData.rewind()
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        // Convert the image to floating point.
        var pixel = 0
        val startTime = SystemClock.uptimeMillis()
        for (i in 0 until DIM_IMG_SIZE_X) {
            for (j in 0 until DIM_IMG_SIZE_Y) {
                val values = intValues[pixel++]
                imgData.putFloat(((values shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((values shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((values and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }
        val endTime = SystemClock.uptimeMillis()
        Log.d(TAG, "Timecost to put values into ByteBuffer: " + (endTime - startTime).toString())
    }

    /**
     * Prints top-K labels, to be shown in UI as the results.
     */
    private fun fetchResults(): Collection<ClassifiedResult> {
        for (i in labelList.indices) {
            sortedLabels.add(
                AbstractMap.SimpleEntry(labelList[i], labelProbArray[0][i])
            )
        }
        return sortedLabels
            .sortedByDescending {
                it.value
            }
            .take(RESULTS_TO_SHOW)
            .map {
                ClassifiedResult(it.key, it.value)
            }
    }
}
