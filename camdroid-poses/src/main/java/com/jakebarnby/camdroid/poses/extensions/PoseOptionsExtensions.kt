package com.jakebarnby.camdroid.poses.extensions

import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jakebarnby.camdroid.models.PoseOptions
import kotlinx.coroutines.asExecutor

object PoseOptionsExtensions {
    fun PoseOptions.toPoseDetectorOptions() = AccuratePoseDetectorOptions.Builder()
        .setExecutor(detectionDispatcher.asExecutor())
        .build()
}