package com.jakebarnby.simpleml.poses.view

import androidx.camera.core.ImageProxy
import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.*
import java.io.Closeable

open class PoseAnalyzerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult> : Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedPose>) -> Unit,
            options: PoseOptions = PoseOptions()
        ): PoseAnalyzerFragment<
                out Analyzer<out Closeable, out Any, ImageProxy, out List<Any>>,
                out Closeable,
                out Any,
                ImageProxy,
                out List<Any>> = when (options.analysisLocation) {
            AnalysisLocation.DEVICE ->
                LocalPoseAnalyzerFragment.newInstance(onNextResult, options)
            AnalysisLocation.FIREBASE_VISION ->
                throw UnsupportedOperationException("No Firebase Vision pose detector available.")
        }
    }
}