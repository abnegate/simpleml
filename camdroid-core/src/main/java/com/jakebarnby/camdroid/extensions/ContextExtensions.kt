package com.jakebarnby.camdroid.extensions

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

fun Context?.getScreenWidth(): Float {
    if (this == null) {
        return 0f
    }

    val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        ?: return 0f

    with(Point()) {
        windowManager.defaultDisplay.getSize(this)
        return x.toFloat()
    }
}

fun Context?.getScreenHeight(): Float {
    if (this == null) {
        return 0f
    }
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        ?: return 0f

    with(Point()) {
        windowManager.defaultDisplay.getSize(this)
        return y.toFloat()
    }
}
