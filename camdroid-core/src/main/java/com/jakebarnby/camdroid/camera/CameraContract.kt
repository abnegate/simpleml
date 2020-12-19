package com.jakebarnby.camdroid.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Size
import com.jakebarnby.camdroid.camera.service.Camera
import com.jakebarnby.camdroid.classification.ClassificationResult
import com.jakebarnby.camdroid.classification.PhotoClassifier

interface CameraContract {

    interface View {
        fun setTextureViewAspectRatio(size: Size)
        fun setTextureViewTransform(matrix: Matrix)

        fun onViewReady(width: Int, height: Int)
        suspend fun onDetectClicked()

        fun showResults(results: Collection<ClassificationResult>)
        fun showError(error: Throwable)
        fun printError(error: Throwable)
    }

    interface Presenter {
        var view: View?
        var camera: Camera?
        var classifier: PhotoClassifier?

        fun subscribe(view: View)
        fun unsubscribe()

        fun openCamera(
            width: Int,
            height: Int,
            orientation: Int,
        )
        fun configureTransform(
            viewWidth: Int,
            viewHeight: Int,
        )
        fun closeCamera()

        suspend fun classify(
            album: Collection<Bitmap>,
            onNextClassificationResult: (Collection<ClassificationResult>) -> Unit
        )
        suspend fun classify(bitmap: Bitmap)
        fun closeClassifier()
    }
}