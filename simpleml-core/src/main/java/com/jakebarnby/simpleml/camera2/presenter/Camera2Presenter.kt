package com.jakebarnby.simpleml.camera2.presenter

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.Camera2Contract
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.suspendCancellableCoroutine

class Camera2Presenter<TDetector, TOptions, TInput, TResult, TOutResult>(
    override var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>,
) : Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult, TOutResult> {

    override var view: Camera2Contract.View<TDetector, TOptions, TInput, TResult, TOutResult>? =
        null

    override fun subscribe(view: Camera2Contract.View<TDetector, TOptions, TInput, TResult, TOutResult>) {
        this.view = view
    }

    override fun unsubscribe() {
        this.view = null
    }

    override fun onBindPreview(rotation: Int) {
        val preview: Preview = Preview.Builder()
            .setTargetName("Preview")
            .build()

        preview.setSurfaceProvider(view?.previewView?.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            IO.asExecutor(),
            analyzer
        )

        val imageCapture = ImageCapture.Builder()
            .setTargetRotation(rotation)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        view?.bindCameraToLifecycle(preview, imageAnalysis, imageCapture)
    }

    @ExperimentalCoroutinesApi
    override suspend fun onCapture(options: ImageCapture.OutputFileOptions) =
        suspendCancellableCoroutine<String?> {
            view?.imageCaptureProvider?.takePicture(
                options,
                IO.asExecutor(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(ex: ImageCaptureException) {
                        it.cancel(ex)
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        it.resume(outputFileResults.savedUri?.toString()) {}
                    }
                }
            )
        }

    override fun onCapture(
        options: ImageCapture.OutputFileOptions,
        onSuccess: (String?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        view?.imageCaptureProvider?.takePicture(
            options,
            IO.asExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(ex: ImageCaptureException) {
                    onError(ex)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onSuccess(outputFileResults.savedUri.toString())
                }
            }
        )
    }
}
