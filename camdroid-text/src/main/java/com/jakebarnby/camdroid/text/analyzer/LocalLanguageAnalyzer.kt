package com.jakebarnby.camdroid.text.analyzer

import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.jakebarnby.camdroid.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class LocalLanguageAnalyzer :
    Analyzer<LanguageIdentifier, LanguageIdentificationOptions, String, List<IdentifiedLanguage>>() {

    override fun initialize(detectorOptions: LanguageIdentificationOptions?) {
        options = detectorOptions ?: LanguageIdentificationOptions.Builder()
            .build()

        detector = LanguageIdentification.getClient(options!!)
    }

    override suspend fun analyzeInput(input: String) {
        val results = detector
            ?.identifyPossibleLanguages(input)
            ?.await() ?: return

        if (results.isNotEmpty()) {
            onAnalysisResult?.invoke(results)
        }
    }
}