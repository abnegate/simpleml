package com.jakebarnby.camdroid.objects

import android.app.Activity
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerTool
import com.jakebarnby.camdroid.models.AnalysisLocation
import com.jakebarnby.camdroid.models.DetectedObject
import com.jakebarnby.camdroid.models.ObjectOptions
import com.jakebarnby.camdroid.objects.camera2.LocalLabelAnalyzer
import com.jakebarnby.camdroid.objects.camera2.LocalObjectAnalyzer
import com.jakebarnby.camdroid.objects.camera2.RemoteLabelAnalyzer
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.asExecutor

class ObjectTool : AnalyzerTool() {

    fun detectObjects(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        options: ObjectOptions = ObjectOptions()
    ) = when (options.analysisLocation) {
        AnalysisLocation.DEVICE ->
            detectObjectsOnDevice(context, onNextResult, options)
        AnalysisLocation.FIREBASE_VISION ->
            throw UnsupportedOperationException("No Firebase Vision object detector available.")
    }

    private fun detectObjectsOnDevice(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        options: ObjectOptions
    ) {
        val realOptions = ObjectDetectorOptions.Builder()
            .setExecutor(options.detectionDispatcher.asExecutor())

        if (options.classificationEnabled) {
            realOptions.enableClassification()
        }
        if (options.detectMultiple) {
            realOptions.enableMultipleObjects()
        }
        detectFromCamera<
                LocalObjectAnalyzer,
                ObjectDetectorOptions,
                List<com.google.mlkit.vision.objects.DetectedObject>>(
            context,
            realOptions.build(),
            { results ->
                onNextResult(results.map { result ->
                    DetectedObject().apply {
                        id = result.trackingId
                        boundingBox = result.boundingBox
                        labels = result.labels.map {
                            Pair(it.text, it.confidence)
                        }
                    }
                })
            })
    }

    fun detectLabels(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        options: ObjectOptions = ObjectOptions()
    ) = when (options.analysisLocation) {
        AnalysisLocation.DEVICE ->
            detectLabelsOnDevice(context, onNextResult, options)
        AnalysisLocation.FIREBASE_VISION ->
            detectLabelsFirebaseVision(context, onNextResult, options)
    }

    private fun detectLabelsOnDevice(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        options: ObjectOptions
    ) = detectFromCamera<LocalLabelAnalyzer, ImageLabelerOptions, List<ImageLabel>>(
        context,
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(options.minimumConfidence)
            .setExecutor(IO.asExecutor())
            .build(),
        { results ->
            onNextResult(results.map { result ->
                DetectedObject().apply {
                    labels = listOf(
                        Pair(result.text, result.confidence)
                    )
                }
            })
        }
    )

    private fun detectLabelsFirebaseVision(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        options: ObjectOptions
    ) = detectFromCamera<
            RemoteLabelAnalyzer,
            FirebaseVisionCloudImageLabelerOptions,
            List<FirebaseVisionImageLabel>>(
        context,
        FirebaseVisionCloudImageLabelerOptions.Builder()
            .setConfidenceThreshold(options.minimumConfidence)
            .build(),
        { results ->
            onNextResult(results.map { result ->
                DetectedObject().apply {
                    labels = listOf(
                        Pair(result.text, result.confidence)
                    )
                }
            })
        }
    )
}