package com.jakebarnby.simpleml.poses.fragment

import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.pose.DetectedPose
import com.jakebarnby.simpleml.models.pose.PoseOptions
import com.jakebarnby.simpleml.models.types.AnalysisLocation

abstract class PoseAnalyzerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult,
        TOutResult> :
    Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult, TOutResult>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedPose>) -> Unit,
            options: PoseOptions = PoseOptions()
        ) = when (options.analysisLocation) {
            AnalysisLocation.DEVICE ->
                LocalPoseAnalyzerFragment.newInstance(onNextResult, options)
        }
    }
}