package com.jakebarnby.camdroid.objects

import android.app.Activity
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerInteractor
import com.jakebarnby.camdroid.models.AnalysisService
import com.jakebarnby.camdroid.models.DetectedObject
import com.jakebarnby.camdroid.objects.camera2.LocalLabelAnalyzer
import com.jakebarnby.camdroid.objects.camera2.LocalObjectAnalyzer
import com.jakebarnby.camdroid.objects.camera2.RemoteLabelAnalyzer
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.asExecutor

class ObjectTool : AnalyzerInteractor() {

    fun detectObjects(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        detectMultiple: Boolean = true
    ) {
        val options = ObjectDetectorOptions.Builder()
            .enableClassification()
            .setExecutor(IO.asExecutor())

        if (detectMultiple) {
            options.enableMultipleObjects()
        }

        startCamera(
            context,
            LocalObjectAnalyzer::class.java,
            options.build(),
            onNextResult
        )
    }

    fun detectLabels(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        minimumConfidence: Float = 0.5f,
        analysisService: AnalysisService = AnalysisService.DEVICE
    ) = when (analysisService) {
        AnalysisService.DEVICE ->
            detectLabelsOnDevice(context, onNextResult, minimumConfidence)
        AnalysisService.FIREBASE_VISION ->
            detectLabelsFirebaseVision(context, onNextResult, minimumConfidence)
    }

    private fun detectLabelsOnDevice(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        minimumConfidence: Float = 0.5f,
    ) = startCamera(
        context,
        LocalLabelAnalyzer::class.java,
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(IO.asExecutor())
            .build(),
        onNextResult
    )

    private fun detectLabelsFirebaseVision(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        minimumConfidence: Float = 0.5f
    ) = startCamera(
        context,
        RemoteLabelAnalyzer::class.java,
        FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .build(),
        onNextResult
    )
}