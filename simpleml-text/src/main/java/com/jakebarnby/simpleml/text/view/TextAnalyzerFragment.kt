package com.jakebarnby.simpleml.text.view

import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.AnalysisLocation
import com.jakebarnby.simpleml.models.DetectedText
import com.jakebarnby.simpleml.models.TextOptions

open class TextAnalyzerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult> : Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult>() {
    companion object {
        fun newInstance(
            onNextResult: (DetectedText) -> Unit,
            options: TextOptions = TextOptions()
        ) = when (options.analysisLocation) {
            AnalysisLocation.DEVICE -> LocalTextAnalyzerFragment.newInstance(onNextResult, options)
            AnalysisLocation.FIREBASE_VISION -> RemoteTextAnalyzerFragment.newInstance(
                onNextResult,
                options
            )
        }
    }
}