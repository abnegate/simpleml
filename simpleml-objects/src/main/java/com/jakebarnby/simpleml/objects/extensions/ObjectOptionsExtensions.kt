package com.jakebarnby.simpleml.objects.extensions

import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import kotlinx.coroutines.asExecutor

object ObjectOptionsExtensions {

    fun ObjectOptions.toImageLabelerOptions(): ImageLabelerOptions =
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(analysisDispatcher.coroutineDispatcher.asExecutor())
            .build()

    fun ObjectOptions.toFirebaseVisionImageLabelerRecognizerOptions() =
        FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .build()

    fun ObjectOptions.toObjectDetectorOptions(): ObjectDetectorOptions {
        val builder = ObjectDetectorOptions.Builder()
            .setExecutor(analysisDispatcher.coroutineDispatcher.asExecutor())

        if (classificationEnabled) {
            builder.enableClassification()
        }
        if (detectMultiple) {
            builder.enableMultipleObjects()
        }

        return builder.build()
    }
}