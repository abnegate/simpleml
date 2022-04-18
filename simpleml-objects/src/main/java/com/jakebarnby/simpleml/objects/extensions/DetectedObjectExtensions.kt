package com.jakebarnby.simpleml.objects.extensions

import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.objects.DetectedObject

object DetectedObjectExtensions {
    fun DetectedObject.toDetectedObject() = com.jakebarnby.simpleml.models.`object`.DetectedObject()
        .apply {
            id = trackingId
            boundingBox = this@toDetectedObject.boundingBox
            labels = this@toDetectedObject.labels.map {
                Pair(it.text, it.confidence)
            }
        }

    fun ImageLabel.toDetectedObject() =
        com.jakebarnby.simpleml.models.`object`.DetectedObject().apply {
            labels = listOf(
                Pair(
                    this@toDetectedObject.text,
                    this@toDetectedObject.confidence
                )
            )
        }
}