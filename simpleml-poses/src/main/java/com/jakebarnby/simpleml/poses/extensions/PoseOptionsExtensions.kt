package com.jakebarnby.simpleml.poses.extensions

import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jakebarnby.simpleml.models.pose.PoseOptions
import kotlinx.coroutines.asExecutor

object PoseOptionsExtensions {
    fun PoseOptions.toPoseDetectorOptions() = AccuratePoseDetectorOptions.Builder()
        .setExecutor(analysisDispatcher.dispatch.asExecutor())
        .build()
}