package com.jakebarnby.simpleml.text.fragment.base

import com.jakebarnby.simpleml.analyzer.Analyzer
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.text.fragment.LocalLanguageAnalyzerFragment

abstract class LanguageAnalyzerFragment<TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
        TDetector,
        TOptions,
        TInput,
        TResult,
        TOutResult> : Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult, TOutResult>() {
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