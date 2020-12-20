package com.jakebarnby.simpleml.camera2

import androidx.camera.lifecycle.ProcessCameraProvider
import com.jakebarnby.simpleml.analyzer.Analyzer

interface Camera2Contract {

    interface View {
        fun checkCameraPermission(): Boolean
        fun startCamera()
        fun bindPreview(cameraProvider: ProcessCameraProvider)
//        fun updateBoundingBox(detectedObject: DetectedObject)
//        fun invalidateOverlay()
//        fun clearOverlay()
    }

    interface Presenter<TDetector, TOptions, TInput, TResult> {
        var view: View?
        var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>

        fun subscribe(view: View)
        fun unsubscribe()
    }
}