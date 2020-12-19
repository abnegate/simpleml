package com.jakebarnby.camdroid.camera.presenter

import android.graphics.Bitmap
import com.jakebarnby.camdroid.camera.service.Camera
import com.jakebarnby.camdroid.camera.CameraContract
import com.jakebarnby.camdroid.classification.ClassificationResult
import com.jakebarnby.camdroid.classification.PhotoClassifier

class CameraPresenter(
    override var camera: Camera?,
    override var classifier: PhotoClassifier?
) : CameraContract.Presenter {

    override var view: CameraContract.View? = null

    override fun subscribe(view: CameraContract.View) {
        this.view = view
    }

    override fun unsubscribe() {
        this.view = null
    }

    override fun openCamera(
        width: Int,
        height: Int,
        orientation: Int
    ) {
        camera?.openCamera(
            width,
            height,
            orientation,
            view!!::setTextureViewAspectRatio
        )
    }

    override fun configureTransform(
        viewWidth: Int,
        viewHeight: Int
    ) {
        view?.setTextureViewTransform(
            camera?.configureTransform(
                viewWidth,
                viewHeight
            ) ?: return
        )
    }

    override fun closeCamera() {
        camera?.close()
    }

    override suspend fun classify(
        album: Collection<Bitmap>,
        onNextClassificationResult: (Collection<ClassificationResult>) -> Unit
    ) = album.forEach {
        classify(it)
    }

    override suspend fun classify(bitmap: Bitmap) {
        try {
            view?.showResults(classifier!!.classify(bitmap))
        } catch (ex: Exception) {
            view?.showError(ex)
        }
    }

    override fun closeClassifier() {
        try {
            classifier!!.close()
        } catch (ex: Exception) {
            view?.showError(ex)
        }
    }
}