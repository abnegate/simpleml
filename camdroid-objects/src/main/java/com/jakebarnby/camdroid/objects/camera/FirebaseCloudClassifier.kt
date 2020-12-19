package com.jakebarnby.camdroid.objects.camera

import android.app.Activity
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.jakebarnby.camdroid.Classification
import com.jakebarnby.camdroid.classification.ClassifiedResult
import com.jakebarnby.camdroid.classification.Classifier
import com.jakebarnby.camdroid.helpers.CoroutineBase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.IOException


/**
 * A [Classifier] implementation using [FirebaseVisionImageLabeler] to classify photos.
 *
 *
 * Created by jbarnby 14/5/2018.
 */
class FirebaseCloudClassifier(
    activity: Activity,
    private val options: FirebaseVisionCloudImageLabelerOptions?,
    private val configuration: Classification.Configuration
) : Classifier, CoroutineBase {

    override val job = Job()

    private lateinit var detector: FirebaseVisionImageLabeler

    companion object {
        private const val TAG = "FirebaseMLVision"
        private const val RESULTS_TO_SHOW = 5
    }

    init {
        FirebaseApp.initializeApp(activity)
    }

    override suspend fun initialise() {
        val visionOptions = options ?: FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(configuration.minimumConfidence!!)
            .build()

        this.detector = FirebaseVision
            .getInstance()
            .getCloudImageLabeler(visionOptions)
    }

    override suspend fun classify(
        album: Collection<Bitmap>,
        onNextClassificationResult: (Collection<ClassifiedResult>) -> Unit
    ) {
        val tasks = mutableListOf<Deferred<Any>>()
        for (bitmap in album) {
            tasks.add(classifyFrameAsync(bitmap))
        }
        tasks.awaitAll()
    }

    override suspend fun classify(bitmap: Bitmap): Collection<ClassifiedResult> =
        classifyFrameAsync(bitmap).await()

    override fun close() {
        try {
            detector.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Classify the given photo and submit the classifications to the result stream.
     *
     * @param photo The photo to classify.
     */
    private fun classifyFrameAsync(photo: Bitmap) = async {
        val startTime = SystemClock.uptimeMillis()
        val result = detector.processImage(FirebaseVisionImage.fromBitmap(photo)).await()
        val endTime = SystemClock.uptimeMillis()

        Log.d(TAG, "Time cost to run model inference: " + (endTime - startTime).toString())

        return@async result
            .sortedByDescending {
                it.confidence
            }
            .take(RESULTS_TO_SHOW)
            .map {
                ClassifiedResult(it.text, it.confidence)
            }
    }
}
