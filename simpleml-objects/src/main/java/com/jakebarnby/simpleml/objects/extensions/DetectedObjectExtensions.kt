package com.jakebarnby.simpleml.objects.extensions

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.objects.DetectedObject

object DetectedObjectExtensions {
    fun DetectedObject.toDetectedObject() = com.jakebarnby.simpleml.models.DetectedObject().apply {
        id = trackingId
        boundingBox = this@toDetectedObject.boundingBox
        labels = this@toDetectedObject.labels.map {
            Pair(it.text, it.confidence)
        }
    }

    fun ImageLabel.toDetectedObject() = com.jakebarnby.simpleml.models.DetectedObject().apply {
        labels = listOf(
            Pair(
                this@toDetectedObject.text,
                this@toDetectedObject.confidence
            )
        )
    }

    fun FirebaseVisionImageLabel.toDetectedObject() =
        com.jakebarnby.simpleml.models.DetectedObject().apply {
            labels = listOf(
                Pair(
                    this@toDetectedObject.text,
                    this@toDetectedObject.confidence
                )
            )
        }
}