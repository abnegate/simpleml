package com.jakebarnby.simpleml.text.extensions

import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.simpleml.models.text.TextOptions
import kotlinx.coroutines.asExecutor

object TextOptionsExtensions {

    fun TextOptions.toTextRecognizerOptions(): TextRecognizerOptions =
        TextRecognizerOptions.Builder()
            .setExecutor(analysisDispatcher.coroutineDispatcher.asExecutor())
            .build()

    fun TextOptions.toFirebaseVisionTextRecognizerOptions() =
        FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)
            .build()

    fun TextOptions.toLanguageIdentificationOptions() =
        LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(analysisDispatcher.coroutineDispatcher.asExecutor())
            .build()
}