package com.jakebarnby.simpleml.camera2

import android.text.method.Touch
import androidx.camera.core.ImageCapture
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.GraphicOverlay

interface Camera2Contract {

    interface View<TDetector, TOptions, TInput, TResult, TOutResult> {
        var presenter: Presenter<TDetector, TOptions, TInput, TResult, TOutResult>?
        var previewView: PreviewView?
        var overlay: GraphicOverlay?
        var cameraProvider: ProcessCameraProvider?
        var imageCaptureProvider: ImageCapture?



        fun checkCameraPermission(): Boolean
        fun startCamera()
        fun setOnNextDetectionListener(onNext: (TOutResult) -> Unit)

        suspend fun takePicture(
            outputPath: String
        ): String?

        fun takePicture(
            outputPath: String,
            onSuccess: (String?) -> Unit,
            onError: (Throwable?) -> Unit
        )

        fun bindCameraToLifecycle(vararg useCases: UseCase)
    }

    interface Presenter<TDetector, TOptions, TInput, TResult, TOutResult> {
        var view: View<TDetector, TOptions, TInput, TResult, TOutResult>?
        var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>

        fun subscribe(view: View<TDetector, TOptions, TInput, TResult, TOutResult>)
        fun unsubscribe()

        fun onBindPreview(rotation: Int)

        suspend fun onCapture(
            options: ImageCapture.OutputFileOptions
        ): String?

        fun onCapture(
            options: ImageCapture.OutputFileOptions,
            onSuccess: (String?) -> Unit,
            onError: (Throwable?) -> Unit
        )
    }
}