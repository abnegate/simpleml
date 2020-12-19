package com.jakebarnby.camdroid.objects.extensions

import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.camdroid.models.ObjectOptions
import kotlinx.coroutines.asExecutor

object ObjectOptionsExtensions {

    fun ObjectOptions.toImageLabelerOptions(): ImageLabelerOptions =
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(detectionDispatcher.asExecutor())
            .build()

    fun ObjectOptions.toFirebaseVisionImageLabelerRecognizerOptions() =
        FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .build()

    fun ObjectOptions.toObjectDetectorOptions(): ObjectDetectorOptions {
        val builder = ObjectDetectorOptions.Builder()
            .setExecutor(detectionDispatcher.asExecutor())

        if (classificationEnabled) {
            builder.enableClassification()
        }
        if (detectMultiple) {
            builder.enableMultipleObjects()
        }

        return builder.build()
    }
}