package com.jakebarnby.camdroid.analyzer

import android.annotation.SuppressLint
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.jakebarnby.camdroid.helpers.CoroutineBase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.Serializable
import kotlin.coroutines.CoroutineContext

abstract class Analyzer<TDetector, TOptions, TInput, TResult>: ImageAnalysis.Analyzer, CoroutineBase, Serializable {

    var detector: TDetector? = null
    var options: TOptions? = null
    var onAnalyzed: ((TResult) -> Unit)? = null

    abstract fun initialize(detectorOptions: TOptions? = null)

    abstract suspend fun analyzeInput(input: TInput)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        launch(Main) {
            try {
                if (image.image == null) {
                    return@launch
                }
                withContext(IO) {
                    this@Analyzer.analyze(image)
                    image.close()
                }
            } catch (ex: CancellationException) {
                ex.printStackTrace()
            }
        }
    }
}
