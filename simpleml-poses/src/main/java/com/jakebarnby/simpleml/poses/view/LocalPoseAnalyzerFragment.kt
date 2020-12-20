package com.jakebarnby.simpleml.poses.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.DetectedPose
import com.jakebarnby.simpleml.models.PoseOptions
import com.jakebarnby.simpleml.poses.analyzer.LocalPoseAnalyzer
import com.jakebarnby.simpleml.poses.extensions.DetectedPoseExtensions.toDetectedPose
import com.jakebarnby.simpleml.poses.extensions.PoseOptionsExtensions.toPoseDetectorOptions

class LocalPoseAnalyzerFragment : PoseAnalyzerFragment<
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