package com.jakebarnby.camdroid.objects.camera2

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.camdroid.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class LocalObjectAnalyzer :
    Analyzer<ObjectDetector, ObjectDetectorOptions, ImageProxy, List<DetectedObject>>() {

    override fun initialize(detectorOptions: ObjectDetectorOptions?) {
        options = detectorOptions ?: ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .build()
        detector = ObjectDetection.getClient(options!!)
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