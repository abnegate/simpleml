package com.jakebarnby.simpleml.poses.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jakebarnby.simpleml.analyzer.Analyzer
import kotlinx.coroutines.tasks.await

class LocalPoseAnalyzer :
    Analyzer<PoseDetector, PoseDetectorOptionsBase, ImageProxy, List<PoseLandmark>>() {

    @ExperimentalGetImage
    override suspend fun analyzeInput(input: ImageProxy) {
        val mlImage = InputImage.fromMediaImage(
            input.image!!,
            input.imageInfo.rotationDegrees
        )
        val results = detector
            ?.process(mlImage)
            ?.await()
            ?.allPoseLandmarks ?: return

        if (results.isNotEmpty()) {
            onAnalysisResult?.invoke(results)
        }
    }

    override fun initialize(detectorOptions: PoseDetectorOptionsBase?) {
        options = detectorOptions ?: AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()

        detector = PoseDetection.getClient(options!!)
    }
}