package com.jakebarnby.camdroid.objects.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.jakebarnby.camdroid.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class RemoteLabelAnalyzer :
    Analyzer<FirebaseVisionImageLabeler, FirebaseVisionCloudImageLabelerOptions, ImageProxy, List<FirebaseVisionImageLabel>>() {

    override fun initialize(detectorOptions: FirebaseVisionCloudImageLabelerOptions?) {
        options = detectorOptions ?: FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()

        detector = FirebaseVision.getInstance().getCloudImageLabeler(options!!)
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

        if (results.isNotEmpty()) {
            onAnalysisResult?.invoke(results)
        }
    }

    private fun degreesToFirebaseRotation(degrees: Int) = when (degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

}