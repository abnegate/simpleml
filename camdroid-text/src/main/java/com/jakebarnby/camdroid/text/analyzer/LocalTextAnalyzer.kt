package com.jakebarnby.camdroid.text.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.camdroid.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class LocalTextAnalyzer: Analyzer<TextRecognizer, TextRecognizerOptions, ImageProxy, Text>() {

    override fun initialize(detectorOptions: TextRecognizerOptions?) {
        options = detectorOptions ?: TextRecognizerOptions.Builder()
            .build()

        detector = TextRecognition.getClient(options!!)
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

        onAnalyzed?.invoke(results)
    }
}