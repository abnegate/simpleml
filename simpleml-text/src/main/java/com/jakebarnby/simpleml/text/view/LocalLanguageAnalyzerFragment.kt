package com.jakebarnby.simpleml.text.view

import androidx.core.os.bundleOf
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.DetectedText
import com.jakebarnby.simpleml.models.TextOptions
import com.jakebarnby.simpleml.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toLanguageIdentificationOptions

class LocalLanguageAnalyzerFragment : LanguageAnalyzerFragment<
        LocalLanguageAnalyzer,
        LanguageIdentifier,
        LanguageIdentificationOptions,
        String,
        List<IdentifiedLanguage>>() {
    companion object {
        fun newInstance(
            text: String,
            onNextResult: (DetectedText) -> Unit,
            options: TextOptions = TextOptions()
        ) = LocalTextAnalyzerFragment().apply {
            arguments =
                bundleOf(Constants.ANALYZER_KEY to BindWrapper(LocalLanguageAnalyzer().apply {
                    onAnalysisResult = {
                        onNextResult(it.toDetectedText(text))
                    }
                    initialize(options.toLanguageIdentificationOptions())
                }))
        }
    }
}