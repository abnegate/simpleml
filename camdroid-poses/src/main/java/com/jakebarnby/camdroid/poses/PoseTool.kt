package com.jakebarnby.camdroid.poses

import android.app.Activity
import android.content.Context
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerInteractor
import com.jakebarnby.camdroid.models.DetectedPose
import com.jakebarnby.camdroid.poses.analyzer.LocalPoseAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class PoseTool : AnalyzerInteractor() {

    fun detectPoses(
        context: Activity,
        onNextResult: (List<DetectedPose>) -> Unit
    ) {
        val options = AccuratePoseDetectorOptions.Builder()
            .setExecutor(Dispatchers.IO.asExecutor())

        startCamera(
            context,
            LocalPoseAnalyzer::class.java,
            options.build(),
            onNextResult
        )
    }
}