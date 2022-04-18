package com.jakebarnby.simpleml.objects.extensions

import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import kotlinx.coroutines.asExecutor

object ObjectOptionsExtensions {

    fun ObjectOptions.toImageLabelerOptions(): ImageLabelerOptions =
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(analysisDispatcher.dispatch.asExecutor())
            .build()

    fun ObjectOptions.toObjectDetectorOptions(): ObjectDetectorOptions {
        val builder = ObjectDetectorOptions.Builder()
            .setExecutor(analysisDispatcher.dispatch.asExecutor())

        if (classificationEnabled) {
            builder.enableClassification()
        }
        if (detectMultiple) {
            builder.enableMultipleObjects()
        }

        return builder.build()
    }
}