package com.jakebarnby.simpleml.objects.fragment.base

import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.`object`.DetectedObject
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.objects.fragment.FirebaseVisionLabelAnalyzerFragment
import com.jakebarnby.simpleml.objects.fragment.LocalLabelAnalyzerFragment

abstract class LabelAnalyzerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult,
        TOutResult> : Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult, TOutResult>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedObject>) -> Unit,
            options: ObjectOptions = ObjectOptions()
        ) = when (options.analysisLocation) {
            AnalysisLocation.DEVICE -> LocalLabelAnalyzerFragment.newInstance(onNextResult, options)
            AnalysisLocation.FIREBASE_VISION -> FirebaseVisionLabelAnalyzerFragment.newInstance(
                onNextResult,
                options
            )
        }
    }
}