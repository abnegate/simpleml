package com.jakebarnby.simpleml.camera2.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.jakebarnby.simpleml.R
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.Camera2Contract
import com.jakebarnby.simpleml.helpers.CoroutineBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asExecutor

class Camera2View<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult> :
    ConstraintLayout,
    LifecycleOwner,
    Camera2Contract.View<TDetector, TOptions, TInput, TResult>,
    CoroutineBase {

    override var presenter: Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult>? = null
    override var previewView: PreviewView? = null
    override var overlay: GraphicOverlay? = null
    override var cameraProvider: ProcessCameraProvider? = null
    override var imageCaptureProvider: ImageCapture? = null

    override val job = Job()

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun init() {
        inflate(context, R.layout.view_camera2, this)

        previewView = findViewById(R.id.preview)
        overlay = findViewById(R.id.overlay)

        @SuppressLint("RestrictedApi")
        if (!CameraX.isInitialized()) {
            CameraX.initialize(context.applicationContext, Camera2Config.defaultConfig())
        }

        startCamera()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.unsubscribe()
    }

    override fun getLifecycle() = (context as LifecycleOwner).lifecycle

    override fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as? AppCompatActivity ?: return false,
                arrayOf(Manifest.permission.CAMERA),
                Camera2Fragment.CAMERA_PERMISSION_CODE
            )
            return false
        }
        return true
    }

    override fun startCamera() {
        if (!checkCameraPermission()) {
            return
        }
        previewView?.post {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                cameraProvider = cameraProviderFuture.get()
                presenter?.onBindPreview(display.rotation)
            }, ContextCompat.getMainExecutor(context))
        }
    }

    override fun capturePreview(options: ImageCapture.OutputFileOptions) {
        presenter?.onCapture(options)
    }

    override fun bindCameraToLifecycle(vararg useCases: UseCase) {
        try {
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                *useCases
            )
        } catch (ex: Exception) {
            Log.e(javaClass.name, "Use case binding failed", ex)
        }
    }
}