package com.jakebarnby.simpleml.text.view

import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.*

open class LanguageAnalyzerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult> : Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult>() {
    companion object {
        fun newInstance(
            text: String,
            onNextResult: (DetectedText) -> Unit,
            options: TextOptions = TextOptions()
        ) = when (options.analysisLocation) {
            AnalysisLocation.DEVICE ->
                LocalLanguageAnalyzerFragment.newInstance(text, onNextResult, options)
            AnalysisLocation.FIREBASE_VISION ->
                throw UnsupportedOperationException("No Firebase Vision detector for language.")
        }
    }
}