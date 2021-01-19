package com.jakebarnby.simpleml.analyzer

import android.annotation.SuppressLint
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.jakebarnby.simpleml.helpers.CoroutineBase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

abstract class Analyzer<TDetector, TOptions, TInput, TResult> :
    ImageAnalysis.Analyzer,
    CoroutineBase {

    override val job = Job()

    var detector: TDetector? = null
    var options: TOptions? = null

    var onAnalysisResult: ((TResult) -> Unit)? = null

    abstract fun initialize(detectorOptions: TOptions? = null)

    abstract suspend fun analyzeInput(input: TInput)

    @SuppressLint("UnsafeExperimentalUsageError")
    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        try {
            if (image.image == null) {
                return
            }
            runBlocking(IO) {
                @Suppress("UNCHECKED_CAST")
                this@Analyzer.analyzeInput(image as TInput)
                image.close()
            }
        } catch (ex: CancellationException) {
            ex.printStackTrace()
        }
    }
}
