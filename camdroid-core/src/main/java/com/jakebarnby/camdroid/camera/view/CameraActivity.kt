package com.jakebarnby.camdroid.camera.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraManager
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Size
import android.view.TextureView
import android.widget.TextView
import com.jakebarnby.camdroid.Constants.UNKNOWN_ERROR
import com.jakebarnby.camdroid.ImageClassifier
import com.jakebarnby.camdroid.R
import com.jakebarnby.camdroid.autofittextureview.AutoFitTextureView
import com.jakebarnby.camdroid.camera.CameraContract
import com.jakebarnby.camdroid.camera.service.CameraService
import com.jakebarnby.camdroid.camera.presenter.CameraPresenter
import com.jakebarnby.camdroid.classification.ClassificationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CameraActivity : AppCompatActivity()
//    CameraContract.View,
//    CoroutineScope
{

//    override val coroutineContext: CoroutineContext
//        get() = job + Dispatchers.IO
//
//    private val job = Job()
//
//    private var checkedPermissions = false
//
//    private lateinit var textView: TextView
//    private lateinit var textureView: AutoFitTextureView
//    private var presenter: CameraContract.Presenter? = null
//
//    /**
//     * [TextureView.SurfaceTextureListener] handles several lifecycle events on an [AutoFitTextureView].
//     */
//    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
//
//        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
//            onViewReady(width, height)
//        }
//
//        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
//            try {
//                presenter?.configureTransform(width, height)
//            } catch (ex: Exception) {
//                printError(ex)
//            }
//        }
//
//        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
//            return true
//        }
//
//        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
//    }
//
//    private val requiredPermissions: Array<String> by lazy {
//        try {
//            val info = packageManager
//                .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
//            val permissions = info.requestedPermissions
//            if (permissions != null && permissions.isNotEmpty()) {
//                permissions
//            } else {
//                emptyArray()
//            }
//        } catch (e: Exception) {
//            emptyArray()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_camera)
//
//        textureView = findViewById(R.id.texture)
//        textView = findViewById(R.id.text)
//
//        findViewById<FloatingActionButton>(R.id.btn_detect)
//            .setOnClickListener {
//                launch {
//                    onDetectClicked()
//                }
//            }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        checkPermissions()
//        if (!textureView.isAvailable) {
//            textureView.surfaceTextureListener = surfaceTextureListener
//        }
//    }
//
//    override fun onPause() {
//        try {
//            presenter?.closeCamera()
//        } catch (ex: Exception) {
//            showError(ex)
//        }
//        presenter?.unsubscribe()
//        super.onPause()
//    }
//
//    override fun onDestroy() {
//        try {
//            presenter?.closeClassifier()
//        } catch (ex: Exception) {
//            showError(ex)
//        }
//        super.onDestroy()
//    }
//
//    override fun onViewReady(width: Int, height: Int) {
//        if (!allPermissionsGranted()) {
//            checkPermissions()
//            return
//        }
//
//        if (presenter == null) {
//            val configuration: ImageClassifier.Configuration? =
//                intent.extras?.getSerializable(ImageClassifier.CONFIG_KEY) as? ImageClassifier.Configuration?
//
//            check(configuration != null) {
//                "Image classifier config is required to start detection activity."
//            }
//
//            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//            val cameraInteractor = CameraService(cameraManager, windowManager, textureView.surfaceTexture!!)
//            val classifier =  com.jakebarnby.camdroid.objects.ObjectClassifierFactory.getFromConfiguration(this, configuration)
//
//            launch {
//                classifier.initialise()
//            }
//
//            presenter = CameraPresenter(cameraInteractor, classifier)
//        }
//
//        presenter?.subscribe(this)
//        presenter?.openCamera(
//            width,
//            height,
//            resources.configuration.orientation)
//    }
//
//    override suspend fun onDetectClicked() {
//        val bitmap = textureView.getBitmap(
//            com.jakebarnby.camdroid.objects.camera.TensorflowClassifier.DIM_IMG_SIZE_X,
//            com.jakebarnby.camdroid.objects.camera.TensorflowClassifier.DIM_IMG_SIZE_Y)
//
//        presenter?.classify(bitmap ?: return)
//    }
//
//    override fun setTextureViewAspectRatio(size: Size) {
//        textureView.setAspectRatio(size.width, size.height)
//    }
//
//    override fun setTextureViewTransform(matrix: Matrix) {
//        textureView.setTransform(matrix)
//    }
//
//    override fun showResults(results: Collection<ClassificationResult>) {
//        val resultBuilder = StringBuilder()
//        results.forEach { result ->
//            resultBuilder.append("${result.label}: ${result.confidence},\n")
//        }
//
//        runOnUiThread {
//            AlertDialog.Builder(this)
//                .setTitle("Detections")
//                .setMessage(resultBuilder.toString())
//                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()}
//                .create()
//                .show()
//        }
//    }
//
//    override fun showError(error: Throwable) {
//        AlertDialog.Builder(this)
//            .setTitle("Error")
//            .setMessage(error.message ?: UNKNOWN_ERROR)
//            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()}
//            .create()
//            .show()
//    }
//
//    override fun printError(error: Throwable) {
//        error.printStackTrace()
//    }
//
//    /**
//     * Opens the camera specified by [CameraActivity.cameraId].
//     */
//    @SuppressLint("MissingPermission")
//    private fun checkPermissions() {
//        if (!checkedPermissions && !allPermissionsGranted()) {
//            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
//            return
//        } else {
//            checkedPermissions = true
//        }
//    }
//
//    private fun allPermissionsGranted(): Boolean {
//        for (permission in requiredPermissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false
//            }
//        }
//        return true
//    }
//
//    companion object {
//        private const val TAG = "TfLite"
//        private const val HANDLE_THREAD_NAME = "CameraBackground"
//        private const val PERMISSIONS_REQUEST_CODE = 1
//    }
}
