package com.jakebarnby.simpleml.poses

import android.app.Activity
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jakebarnby.simpleml.analyzer.Detector
import com.jakebarnby.simpleml.models.pose.DetectedPose
import com.jakebarnby.simpleml.models.pose.PoseOptions
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.poses.analyzer.LocalPoseAnalyzer
import com.jakebarnby.simpleml.poses.extensions.DetectedPoseExtensions.toDetectedPose
import kotlinx.coroutines.asExecutor

class PoseDetector : Detector() {

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
            .setExecutor(options.analysisDispatcher.coroutineDispatcher.asExecutor())

        startDetectorActivity<LocalPoseAnalyzer, PoseDetector, PoseDetectorOptionsBase, ImageProxy, List<PoseLandmark>>(
            context,
            realOptions.build(),
            { results ->
                onNextResult(results.map { it.toDetectedPose() })
            }
        )
    }
}