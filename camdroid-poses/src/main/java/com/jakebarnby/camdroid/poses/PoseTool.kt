package com.jakebarnby.camdroid.poses

import android.app.Activity
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerTool
import com.jakebarnby.camdroid.models.AnalysisLocation
import com.jakebarnby.camdroid.models.DetectedPose
import com.jakebarnby.camdroid.models.PoseOptions
import com.jakebarnby.camdroid.poses.analyzer.LocalPoseAnalyzer
import com.jakebarnby.camdroid.poses.extensions.DetectedPoseExtensions.toDetectedPose
import kotlinx.coroutines.asExecutor

class PoseTool : AnalyzerTool() {

    fun detectPoses(
        context: Activity,
        onNextResult: (List<DetectedPose>) -> Unit,
        options: PoseOptions = PoseOptions()
    ) = when (options.analysisLocation) {
        AnalysisLocation.DEVICE ->
            detectPosesOnDevice(context, onNextResult, options)
        AnalysisLocation.FIREBASE_VISION ->
            throw UnsupportedOperationException("No Firebase Vision pose detector available.")
    }

    private fun detectPosesOnDevice(
        context: Activity,
        onNextResult: (List<DetectedPose>) -> Unit,
        options: PoseOptions = PoseOptions()
    ) {
        val realOptions = AccuratePoseDetectorOptions.Builder()
            .setExecutor(options.detectionDispatcher.asExecutor())

        startCameraActivity<LocalPoseAnalyzer, PoseDetector, PoseDetectorOptionsBase, ImageProxy, List<PoseLandmark>>(
            context,
            realOptions.build(),
            { results ->
                onNextResult(results.map { it.toDetectedPose() })
            }
        )
    }
}