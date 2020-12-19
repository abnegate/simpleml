package com.jakebarnby.camdroid.objects.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.models.DetectedObject
import com.jakebarnby.camdroid.models.ObjectOptions
import com.jakebarnby.camdroid.objects.analyzer.RemoteLabelAnalyzer
import com.jakebarnby.camdroid.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.camdroid.objects.extensions.ObjectOptionsExtensions.toFirebaseVisionImageLabelerRecognizerOptions

class RemoteLabelAnalyzerFragment : Camera2Fragment<
        RemoteLabelAnalyzer,
        FirebaseVisionImageLabeler,
        FirebaseVisionCloudImageLabelerOptions,
        ImageProxy,
        List<FirebaseVisionImageLabel>>() 
{
    companion object {
        fun newInstance(
            options: ObjectOptions,
            onNextResult: (List<DetectedObject>) -> Unit
        ) = RemoteLabelAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(RemoteLabelAnalyzer().apply {
                onAnalysisResult = { results ->
                    onNextResult(results.map { it.toDetectedObject() })
                }
                initialize(options.toFirebaseVisionImageLabelerRecognizerOptions())
            }))
        }
    }
}