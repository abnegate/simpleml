package com.jakebarnby.camdroid.camera.service

import android.graphics.Matrix
import android.util.Size
import java.io.Closeable

interface Camera : Closeable {

    fun openCamera(
        width: Int,
        height: Int,
        orientation: Int,
        onCameraOutputSet: (Size) -> Unit,
    )

    fun configureTransform(
        viewWidth: Int,
        viewHeight: Int,
    ): Matrix
}