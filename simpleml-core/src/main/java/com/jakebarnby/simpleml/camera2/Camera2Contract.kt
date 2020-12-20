package com.jakebarnby.simpleml.camera2

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.GraphicOverlay

interface Camera2Contract {

    interface View<TDetector, TOptions, TInput, TResult> {
        var presenter: Presenter<TDetector, TOptions, TInput, TResult>?
        var previewView: PreviewView?
        var overlay: GraphicOverlay?
        var cameraProvider: ProcessCameraProvider?
        var imageCaptureProvider: ImageCapture?

        fun checkCameraPermission(): Boolean
        fun startCamera()
        fun capturePreview(options: ImageCapture.OutputFileOptions)
        fun bindCameraToLifecycle(vararg useCases: UseCase)
    }

    interface Presenter<TDetector, TOptions, TInput, TResult> {
        var view: View<TDetector, TOptions, TInput, TResult>?
        var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>

        fun subscribe(view: View<TDetector, TOptions, TInput, TResult>)
        fun unsubscribe()

        fun onBindPreview(rotation: Int)
        fun onCapture(options: ImageCapture.OutputFileOptions)
    }
}