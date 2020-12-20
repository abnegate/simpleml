package com.jakebarnby.simpleml.camera2.presenter

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.Camera2Contract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class Camera2Presenter<TDetector, TOptions, TInput, TResult>(
    override var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>,
) : Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult> {

    override var view: Camera2Contract.View<TDetector, TOptions, TInput, TResult>? = null

    override fun subscribe(view: Camera2Contract.View<TDetector, TOptions, TInput, TResult>) {
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
            Dispatchers.IO.asExecutor(),
            analyzer
        )

        val imageCapture = ImageCapture.Builder()
            .setTargetRotation(rotation)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        view?.bindCameraToLifecycle(preview, imageAnalysis, imageCapture)
    }

    override fun onCapture(options: ImageCapture.OutputFileOptions) {
        TODO("Not yet implemented")
    }
}