package com.jakebarnby.simpleml.text.extensions

import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jakebarnby.simpleml.models.text.TextOptions
import kotlinx.coroutines.asExecutor

object TextOptionsExtensions {

    fun TextOptions.toTextRecognizerOptions(): TextRecognizerOptions =
        TextRecognizerOptions.Builder()
            .setExecutor(analysisDispatcher.dispatch.asExecutor())
            .build()

    fun TextOptions.toLanguageIdentificationOptions() =
        LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(analysisDispatcher.dispatch.asExecutor())
            .build()
}