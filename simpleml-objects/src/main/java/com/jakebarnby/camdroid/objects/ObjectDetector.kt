package com.jakebarnby.simpleml.objects

import android.app.Activity
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.simpleml.analyzer.AnalyzerTool
import com.jakebarnby.simpleml.models.AnalysisLocation
import com.jakebarnby.simpleml.models.DetectedObject
import com.jakebarnby.simpleml.models.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.LocalLabelAnalyzer
import com.jakebarnby.simpleml.objects.analyzer.LocalObjectAnalyzer
import com.jakebarnby.simpleml.objects.analyzer.RemoteLabelAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toFirebaseVisionImageLabelerRecognizerOptions
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toImageLabelerOptions
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toObjectDetectorOptions

class ObjectDetector : AnalyzerTool() {

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
        startCameraActivity<
                LocalObjectAnalyzer,
                ObjectDetector,
                ObjectDetectorOptions,
                ImageProxy,
                List<com.google.mlkit.vision.objects.DetectedObject>>(
            context,
            options.toObjectDetectorOptions(),
            { results ->
                onNextResult(results.map { it.toDetectedObject() })
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
    ) = startCameraActivity<
            LocalLabelAnalyzer,
            ImageLabeler,
            ImageLabelerOptions,
            ImageProxy,
            List<ImageLabel>>(
        context,
        options.toImageLabelerOptions(),
        { results ->
            onNextResult(results.map { it.toDetectedObject() })
        }
    )

    private fun detectLabelsFirebaseVision(
        context: Activity,
        onNextResult: (List<DetectedObject>) -> Unit,
        options: ObjectOptions
    ) = startCameraActivity<
            RemoteLabelAnalyzer,
            FirebaseVisionImageLabeler,
            FirebaseVisionCloudImageLabelerOptions,
            ImageProxy,
            List<FirebaseVisionImageLabel>>(
        context,
        options.toFirebaseVisionImageLabelerRecognizerOptions(),
        { results ->
            onNextResult(results.map { it.toDetectedObject() })
        }
    )
}