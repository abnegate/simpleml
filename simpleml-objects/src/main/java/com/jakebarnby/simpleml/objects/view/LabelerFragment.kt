package com.jakebarnby.simpleml.objects.view

import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.AnalysisLocation
import com.jakebarnby.simpleml.models.DetectedObject
import com.jakebarnby.simpleml.models.ObjectOptions

open class LabelerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult> : Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedObject>) -> Unit,
            options: ObjectOptions = ObjectOptions()
        ) = when (options.analysisLocation) {
            AnalysisLocation.DEVICE -> LocalLabelAnalyzerFragment.newInstance(onNextResult, options)
            AnalysisLocation.FIREBASE_VISION -> RemoteLabelAnalyzerFragment.newInstance(
                onNextResult,
                options
            )
        }
    }
}