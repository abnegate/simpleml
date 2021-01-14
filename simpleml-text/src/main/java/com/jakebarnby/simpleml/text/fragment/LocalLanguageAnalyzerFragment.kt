package com.jakebarnby.simpleml.text.fragment

import androidx.core.os.bundleOf
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.text.DetectedLanguage
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedLanguageExtensions.toDetectedLanguage
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toLanguageIdentificationOptions
import com.jakebarnby.simpleml.text.fragment.base.LanguageAnalyzerFragment

class LocalLanguageAnalyzerFragment : LanguageAnalyzerFragment<
        LocalLanguageAnalyzer,
        LanguageIdentifier,
        LanguageIdentificationOptions,
        String,
        List<IdentifiedLanguage>,
        List<DetectedLanguage>>() {
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

    override fun setOnNextDetectionListener(onNext: (List<DetectedLanguage>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedLanguage() })
        }
    }
}