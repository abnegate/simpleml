package com.jakebarnby.simpleml.text.extensions

import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.jakebarnby.simpleml.models.text.DetectedLanguage

object DetectedLanguageExtensions {

    fun IdentifiedLanguage.toDetectedLanguage() =
        DetectedLanguage().apply {
            language = this@toDetectedLanguage.languageTag
            confidence = this@toDetectedLanguage.confidence
        }
}