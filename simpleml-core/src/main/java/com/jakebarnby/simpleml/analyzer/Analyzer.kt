package com.jakebarnby.simpleml.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class Analyzer<TDetector, TOptions, TInput, TResult> :
    ImageAnalysis.Analyzer,
    CoroutineBase {

    override val job = Job()

    var detector: TDetector? = null
    var options: TOptions? = null

    var onAnalysisResult: ((TResult) -> Unit)? = null

    abstract fun initialize(detectorOptions: TOptions? = null)

    abstract suspend fun analyzeInput(input: TInput)

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        try {
            image.image ?: return

            launch(AnalysisDispatcher.IO.dispatch) {
                @Suppress("UNCHECKED_CAST")
                this@Analyzer.analyzeInput(image as TInput)
                image.close()
            }
        } catch (ex: CancellationException) {
            ex.printStackTrace()
        }
    }
}
