package com.jakebarnby.simpleml.camera2.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jakebarnby.simpleml.Constants.ANALYZER_KEY
import com.jakebarnby.simpleml.R
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.Camera2Contract
import com.jakebarnby.simpleml.camera2.presenter.Camera2Presenter
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.helpers.CoroutineBase
import kotlinx.coroutines.Job
import java.io.File

abstract class Camera2Fragment<
        TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult,
        TOutResult> :
    Fragment(R.layout.fragment_camera2),
    Camera2Contract.View<TDetector, TOptions, TInput, TResult, TOutResult>,
    CoroutineBase {

    override val job = Job()

    companion object {
        const val CAMERA_PERMISSION_CODE = 0x01
    }

    override var presenter: Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult, TOutResult>? =
        null
    override var previewView: PreviewView? = null
    override var overlay: GraphicOverlay? = null
    override var cameraProvider: ProcessCameraProvider? = null
    override var imageCaptureProvider: ImageCapture? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val analyzerWrapper = arguments?.getBinder(ANALYZER_KEY) as? BindWrapper<TAnalyzer>
            ?: throw IllegalStateException("No analyzer wrapper found.")

        val analyzer = analyzerWrapper.data as? TAnalyzer
            ?: throw IllegalStateException("No analyzer found.")

        presenter = Camera2Presenter(analyzer)
        previewView = view.findViewById(R.id.preview)
        overlay = view.findViewById(R.id.overlay)

        @SuppressLint("RestrictedApi")
        if (!CameraX.isInitialized()) {
            CameraX.initialize(requireContext().applicationContext, Camera2Config.defaultConfig())
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
                //TODO: Handle permission denied
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
        val context = requireContext()
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
            return false
        }
        return true
    }

    override fun startCamera() {
        val context = requireContext()
        if (!checkCameraPermission()) {
            return
        }
        previewView?.post {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                cameraProvider = cameraProviderFuture.get()
                presenter?.onBindPreview(view?.display?.rotation ?: 0)
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
