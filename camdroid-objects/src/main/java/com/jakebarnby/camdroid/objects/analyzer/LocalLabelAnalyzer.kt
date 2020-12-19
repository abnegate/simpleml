package com.jakebarnby.camdroid.objects.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.jakebarnby.camdroid.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class LocalLabelAnalyzer :
    Analyzer<ImageLabeler, ImageLabelerOptions, ImageProxy, List<ImageLabel>>() {

    override fun initialize(detectorOptions: ImageLabelerOptions?) {
        options = detectorOptions ?: ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()

        detector = ImageLabeling.getClient(options!!)
    }

    @ExperimentalGetImage
    override suspend fun analyzeInput(input: ImageProxy) {
        val mlImage = InputImage.fromMediaImage(
            input.image!!,
            input.imageInfo.rotationDegrees
        )

        val results = detector
            ?.process(mlImage)
            ?.await() ?: return

        if (results.isNotEmpty()) {
            onAnalysisResult?.invoke(results)
        }
    }
}