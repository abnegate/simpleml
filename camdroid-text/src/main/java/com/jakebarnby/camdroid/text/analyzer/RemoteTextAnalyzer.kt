package com.jakebarnby.camdroid.text.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.jakebarnby.camdroid.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class RemoteTextAnalyzer: Analyzer<FirebaseVisionTextRecognizer, FirebaseVisionCloudTextRecognizerOptions, ImageProxy, FirebaseVisionText>() {

    override fun initialize(detectorOptions: FirebaseVisionCloudTextRecognizerOptions?) {
        options = detectorOptions ?: FirebaseVisionCloudTextRecognizerOptions.Builder()
            .build()

        detector = FirebaseVision.getInstance().getCloudTextRecognizer(options!!)
    }

    @ExperimentalGetImage
    override suspend fun analyzeInput(input: ImageProxy) {
        val mlImage = FirebaseVisionImage.fromMediaImage(
            input.image!!,
            degreesToFirebaseRotation(input.imageInfo.rotationDegrees)
        )

        val results = detector
            ?.processImage(mlImage)
            ?.await() ?: return

        onAnalyzed?.invoke(results)
    }

    private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }
}