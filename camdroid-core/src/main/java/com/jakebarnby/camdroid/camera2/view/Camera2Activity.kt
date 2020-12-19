package com.jakebarnby.camdroid.camera2.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.R
import com.jakebarnby.camdroid.analyzer.Analyzer
import com.jakebarnby.camdroid.camera2.Camera2Contract
import com.jakebarnby.camdroid.camera2.presenter.Camera2Presenter
import com.jakebarnby.camdroid.helpers.BindWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.asExecutor
import kotlin.coroutines.CoroutineContext

class Camera2Activity<
        TDetector,
        TOptions,
        TInput,
        TResult,
        TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>> :
    AppCompatActivity(), Camera2Contract.View, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val job = Job()

    companion object {
        const val CAMERA_PERMISSION_CODE = 0x01
    }

    private var presenter: Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult>? = null

    private var previewView: PreviewView? = null
    private var overlay: GraphicOverlay? = null
    private var backBtn: ImageButton? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)

        val analyzerWrapper = intent.extras?.getBinder(
            Constants.ANALYZER_KEY
        ) as? BindWrapper<TAnalyzer>
            ?: throw IllegalStateException("No analyzer wrapper found!")

        val analyzer = analyzerWrapper.data as? TAnalyzer
            ?: throw IllegalStateException("No analyzer found!")

        presenter = Camera2Presenter(analyzer)
        previewView = findViewById(R.id.preview)
        overlay = findViewById(R.id.overlay)
        backBtn = findViewById(R.id.btnBack)

        backBtn?.setOnClickListener { onBackPressed() }

        if (!CameraX.isInitialized()) {
            CameraX.initialize(applicationContext, Camera2Config.defaultConfig())
        }

        startCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.subscribe(this)
    }

    override fun onPause() {
        presenter?.unsubscribe()
        super.onPause()
    }

    override fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
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
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            }, ContextCompat.getMainExecutor(this))
        }
    }

    override fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .setTargetName("Preview")
            .build()

        preview.setSurfaceProvider(previewView?.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            IO.asExecutor(),
            presenter!!.analyzer
        )

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        } catch (ex: Exception) {
            Log.e(javaClass.name, "Use case binding failed", ex)
        }
    }
}
