package com.jakebarnby.camdroid.text.view

import androidx.core.os.bundleOf
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.models.DetectedText
import com.jakebarnby.camdroid.models.TextOptions
import com.jakebarnby.camdroid.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.camdroid.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.camdroid.text.extensions.TextOptionsExtensions.toLanguageIdentificationOptions

class LocalLanguageAnalyzerFragment :
    Camera2Fragment<LocalLanguageAnalyzer, LanguageIdentifier, LanguageIdentificationOptions, String, List<IdentifiedLanguage>>() {
    companion object {
        fun newInstance(
            text: String,
            options: TextOptions,
            onNextResult: (DetectedText) -> Unit
        ) = LocalTextAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(LocalLanguageAnalyzer().apply {
                onAnalysisResult = {
                    onNextResult(it.toDetectedText(text))
                }
                initialize(options.toLanguageIdentificationOptions())
            }))
        }
    }
}