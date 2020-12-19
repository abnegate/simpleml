package com.jakebarnby.camdroid.camera.service

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.WindowManager
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class CameraService(
    private val cameraManager: CameraManager,
    private val windowManager: WindowManager,
    private val surfaceTexture: SurfaceTexture
): Camera {

    private var cameraId: String? = null
    private var captureSession: CameraCaptureSession? = null
    private var cameraDevice: CameraDevice? = null
    private var previewSize: Size? = null

    private var imageReader: ImageReader? = null
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var previewRequest: CaptureRequest? = null

    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private val cameraOpenCloseLock = Semaphore(1) // To prevent the app from exiting before closing the camera.

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
        ) {
        }

        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
        }
    }

    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(currentCameraDevice: CameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            cameraOpenCloseLock.release()
            cameraDevice = currentCameraDevice
            createCameraPreviewSession(surfaceTexture) { it.printStackTrace() }
        }

        override fun onDisconnected(currentCameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            currentCameraDevice.close()
            cameraDevice = null
        }

        override fun onError(currentCameraDevice: CameraDevice, error: Int) {
            cameraOpenCloseLock.release()
            currentCameraDevice.close()
            cameraDevice = null
        }
    }

    init {
        startBackgroundThread()
    }

    @SuppressLint("MissingPermission")
    @Throws(
        CameraAccessException::class,
        InterruptedException::class,
        TimeoutException::class
    )
    override fun openCamera(
        width: Int,
        height: Int,
        orientation: Int,
        onCameraOutputSet: (Size) -> Unit,
    ) {

        setUpCameraOutputs(width, height, orientation, onCameraOutputSet)
        configureTransform(width, height)

        if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
            throw TimeoutException("Time out waiting to lock cameraManager opening.")
        }
        cameraManager.openCamera(cameraId!!, stateCallback, backgroundHandler)
    }

    override fun close() {
        try {
            cameraOpenCloseLock.acquire()
            if (captureSession != null) {
                captureSession?.close()
                captureSession = null
            }
            if (cameraDevice != null) {
                cameraDevice?.close()
                cameraDevice = null
            }
            if (imageReader != null) {
                imageReader?.close()
                imageReader = null
            }
            System.gc()
        } catch (e: InterruptedException) {
            throw e
        } finally {
            stopBackgroundThread()
            cameraOpenCloseLock.release()
        }
    }

    /**
     * Configures the necessary [android.graphics.Matrix] transformation to `textureView`. This
     * method should be called after the camera preview size is determined in setUpCameraOutputs and
     * also the size of `textureView` is fixed.
     *
     * @param viewWidth  The width of `textureView`
     * @param viewHeight The height of `textureView`
     */
    override fun configureTransform(
        viewWidth: Int,
        viewHeight: Int
    ): Matrix {

        if (previewSize == null) {
            throw IllegalStateException("No previewSize set.")
        }

        val rotation = windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect =
            RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                viewHeight.toFloat() / previewSize!!.height,
                viewWidth.toFloat() / previewSize!!.width
            )
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        return matrix
    }

    @Throws(CameraAccessException::class)
    private fun setUpCameraOutputs(
        width: Int,
        height: Int,
        orientation: Int,
        onCameraOutputSet: (Size) -> Unit,
    ) {
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)

            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }

            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                ?: continue

            // For still image captures, we use the largest available size.
            val largest = Collections.max(
                Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
                CompareSizesByArea()
            )

            imageReader = ImageReader.newInstance(
                largest.width,
                largest.height,
                ImageFormat.JPEG,
                2
            )

            // Find out if we need to swap dimension to get the preview size relative to sensor coordinate.
            val displayRotation = windowManager.defaultDisplay.rotation

            /* Orientation of the camera sensor */
            val sensorOrientation =
                characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
            var swappedDimensions = false
            when (displayRotation) {
                Surface.ROTATION_0, Surface.ROTATION_180 -> if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true
                }
                Surface.ROTATION_90, Surface.ROTATION_270 -> if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true
                }
                else -> Log.e(TAG, "Display rotation is invalid: $displayRotation")
            }

            val displaySize = Point().apply {
                windowManager.defaultDisplay.getSize(this)
            }

            var rotatedPreviewWidth = width
            var rotatedPreviewHeight = height
            var maxPreviewWidth = displaySize.x
            var maxPreviewHeight = displaySize.y

            if (swappedDimensions) {
                rotatedPreviewWidth = height
                rotatedPreviewHeight = width
                maxPreviewWidth = displaySize.y
                maxPreviewHeight = displaySize.x
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT
            }

            previewSize = chooseOptimalSize(
                map.getOutputSizes(SurfaceTexture::class.java),
                rotatedPreviewWidth,
                rotatedPreviewHeight,
                maxPreviewWidth,
                maxPreviewHeight,
                largest
            )

            // We fit the aspect ratio of TextureView to the size of preview we picked.
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                onCameraOutputSet(Size(previewSize!!.width, previewSize!!.height))
            } else {
                onCameraOutputSet(Size(previewSize!!.height, previewSize!!.width))
            }
            this.cameraId = cameraId
        }
    }

    @Throws(CameraAccessException::class)
    private fun createCameraPreviewSession(texture: SurfaceTexture, onError: (Throwable) -> Unit) {
        texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)

        val surface = Surface(texture)

        previewRequestBuilder =
            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder!!.addTarget(surface)

        cameraDevice!!.createCaptureSession(
            listOf(surface),
            object : CameraCaptureSession.StateCallback() {

                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    if (null == cameraDevice) {
                        return // The camera is already closed
                    }

                    captureSession = cameraCaptureSession

                    // Auto focus should be continuous.
                    previewRequestBuilder!!.set(
                        CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )

                    previewRequest = previewRequestBuilder?.build()
                    captureSession?.setRepeatingRequest(
                        previewRequest!!,
                        captureCallback,
                        backgroundHandler
                    )
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                    onError(RuntimeException("Failed to configure camera preview."))
                }
            }, null)
    }

    /**
     * Starts a background thread and its [Handler].
     */
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread(HANDLE_THREAD_NAME)
        backgroundThread?.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val TAG = "TfLiteCamera"
        private const val HANDLE_THREAD_NAME = "CameraBackground"
        private const val MAX_PREVIEW_WIDTH = 1920
        private const val MAX_PREVIEW_HEIGHT = 1080

        /**
         * Resizes image.
         *
         *
         * Attempting to use too large a preview size could  exceed the camera bus' bandwidth limitation,
         * resulting in gorgeous previews but the storage of garbage capture data.
         *
         *
         * Given `choices` of `Size`s supported by a camera, choose the smallest one that is
         * at least as large as the respective texture view size, and that is at most as large as the
         * respective max size, and whose aspect ratio matches with the specified value. If such size
         * doesn't exist, choose the largest one that is at most as large as the respective max size, and
         * whose aspect ratio matches with the specified value.
         *
         * @param choices           The list of sizes that the camera supports for the intended output class
         * @param textureViewWidth  The width of the texture view relative to sensor coordinate
         * @param textureViewHeight The height of the texture view relative to sensor coordinate
         * @param maxWidth          The maximum width that can be chosen
         * @param maxHeight         The maximum height that can be chosen
         * @param aspectRatio       The aspect ratio
         * @return The optimal `Size`, or an arbitrary one if none were big enough
         */
        private fun chooseOptimalSize(
            choices: Array<Size>,
            textureViewWidth: Int,
            textureViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size
        ): Size {
            // Collect the supported resolutions that are at least as big as the preview Surface
            val bigEnough = ArrayList<Size>()
            // Collect the supported resolutions that are smaller than the preview Surface
            val notBigEnough = ArrayList<Size>()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices) {
                if (option.width <= maxWidth
                    && option.height <= maxHeight
                    && option.height == option.width * h / w
                ) {
                    if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            // Pick the smallest of those big enough. If there is no one big enough, pick the
            // largest of those not big enough.
            return when {
                bigEnough.size > 0 -> Collections.min(
                    bigEnough,
                    CompareSizesByArea()
                )
                notBigEnough.size > 0 -> Collections.max(
                    notBigEnough,
                    CompareSizesByArea()
                )
                else -> {
                    Log.e(TAG, "Couldn't find any suitable preview size")
                    choices[0]
                }
            }
        }
    }

    /**
     * Compares two `Size`s based on their areas.
     */
    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            // We cast here to ensure the multiplications won't overflow
            return java.lang.Long.signum(
                lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height
            )
        }
    }
}