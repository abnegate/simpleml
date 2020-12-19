package com.jakebarnby.camdroid.poses.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.models.DetectedPose
import com.jakebarnby.camdroid.models.PoseOptions
import com.jakebarnby.camdroid.poses.analyzer.LocalPoseAnalyzer
import com.jakebarnby.camdroid.poses.extensions.DetectedPoseExtensions.toDetectedPose
import com.jakebarnby.camdroid.poses.extensions.PoseOptionsExtensions.toPoseDetectorOptions

class LocalPoseAnalyzerFragment : Camera2Fragment<
        LocalPoseAnalyzer,
        PoseDetector,
        PoseDetectorOptionsBase,
        ImageProxy,
        List<PoseLandmark>>()
{
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedPose>) -> Unit,
            options: PoseOptions = PoseOptions(),
        ) = LocalPoseAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(LocalPoseAnalyzer().apply {
                onAnalysisResult = { results ->
                    onNextResult(results.map { it.toDetectedPose() })
                }
                initialize(options.toPoseDetectorOptions())
            }))
        }
    }
}