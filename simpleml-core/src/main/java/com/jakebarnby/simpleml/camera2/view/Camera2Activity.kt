package com.jakebarnby.simpleml.camera2.view

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jakebarnby.simpleml.Constants.ANALYZER_KEY
import com.jakebarnby.simpleml.R
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.Camera2Contract
import com.jakebarnby.simpleml.camera2.presenter.Camera2Presenter
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.helpers.CoroutineBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class Camera2Activity<
        TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult,
        TOutResult> :
    AppCompatActivity(),
    Camera2Contract.View<TDetector, TOptions, TInput, TResult, TOutResult>,
    CoroutineBase {

    override val job = Job()

    companion object {
        const val CAMERA_PERMISSION_CODE = 0x01
    }

    override var presenter: Camera2Contract.Presenter<
            TDetector,
            TOptions,
            TInput,
            TResult,
            TOutResult
            >? = null

    override var previewView: PreviewView? = null
    override var overlay: GraphicOverlay? = null
    override var cameraProvider: ProcessCameraProvider? = null
    override var imageCaptureProvider: ImageCapture? = null

    private var backBtn: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)

        @Suppress("UNCHECKED_CAST")
        val analyzerWrapper = intent.extras?.getBinder(ANALYZER_KEY) as? BindWrapper<TAnalyzer>
            ?: throw IllegalStateException("No analyzer wrapper found!")

        val analyzer = analyzerWrapper.data as? TAnalyzer
            ?: throw IllegalStateException("No analyzer found!")

        presenter = Camera2Presenter(analyzer)
        previewView = findViewById(R.id.preview)
        overlay = findViewById(R.id.overlay)
        backBtn = findViewById(R.id.btnBack)

        backBtn?.setOnClickListener { onBackPressed() }

        startCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

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
                cameraProvider = cameraProviderFuture.get()
                presenter?.onBindPreview(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        display?.rotation ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        windowManager.defaultDisplay.rotation
                    }
                )
            }, ContextCompat.getMainExecutor(this))
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

    override fun setOnNextDetectionListener(onNext: (TOutResult) -> Unit) {
        TODO("Not yet implemented")
    }
}
