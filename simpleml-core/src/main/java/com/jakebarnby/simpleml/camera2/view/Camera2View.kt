package com.jakebarnby.simpleml.camera2.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.jakebarnby.simpleml.R
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.Camera2Contract
import com.jakebarnby.simpleml.camera2.presenter.Camera2Presenter
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.models.OptionsBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
abstract class Camera2View<TDetector, TOptions, TInput, TResult, TOutResult> :
    FrameLayout,
    LifecycleOwner,
    LifecycleObserver,
    Camera2Contract.View<TDetector, TOptions, TInput, TResult, TOutResult>,
    CoroutineBase {

    override var presenter: Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult, TOutResult>? =
        null
    override var previewView: PreviewView? = null
    override var overlay: GraphicOverlay? = null
    override var cameraProvider: ProcessCameraProvider? = null
    override var imageCaptureProvider: ImageCapture? = null

    var options: OptionsBase? = null

    override val job = Job()

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

    open fun init(attrs: AttributeSet) {
        inflate(context, R.layout.view_camera2, this)

        previewView = findViewById(R.id.preview)
        overlay = findViewById(R.id.overlay)

        (context as LifecycleOwner)
            .lifecycle
            .addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        cameraProvider?.unbindAll()
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

    inline fun <reified TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>> setAnalyzer(
        noinline onNextResult: (TResult) -> Unit,
        options: TOptions
    ) {
        val analyzer = TAnalyzer::class.java.newInstance().apply {
            onAnalysisResult = onNextResult
            initialize(options)
        }
        presenter = Camera2Presenter(analyzer)
        presenter?.subscribe(this)
        startCamera()
    }

    protected inline fun <reified TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>> setAnalyzer(
        options: TOptions
    ) {
        val analyzer = TAnalyzer::class.java.newInstance().apply {
            initialize(options)
        }
        presenter = Camera2Presenter(analyzer)
        presenter?.subscribe(this)
        startCamera()
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

    override suspend fun takePicture(outputPath: String) =
        presenter?.onCapture(ImageCapture.OutputFileOptions.Builder(File(outputPath)).build())

    override fun takePicture(
        outputPath: String,
        onSuccess: (String?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        presenter?.onCapture(
            ImageCapture.OutputFileOptions.Builder(File(outputPath)).build(),
            onSuccess,
            onError
        )
    }

    override fun bindCameraToLifecycle(vararg useCases: UseCase) {
        val newCases = useCases.filter {
            cameraProvider?.isBound(it) == false
        }.toTypedArray()

        try {
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(
                { lifecycle },
                CameraSelector.DEFAULT_BACK_CAMERA,
                *newCases
            )
        } catch (ex: Exception) {
            Log.e(javaClass.name, "Use case binding failed", ex)
        }
    }
}