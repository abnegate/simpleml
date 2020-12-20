package com.jakebarnby.simpleml.objects.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.DetectedObject
import com.jakebarnby.simpleml.models.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.RemoteLabelAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toFirebaseVisionImageLabelerRecognizerOptions

class RemoteLabelAnalyzerFragment : ObjectAnalyzerFragment<
        RemoteLabelAnalyzer,
        FirebaseVisionImageLabeler,
        FirebaseVisionCloudImageLabelerOptions,
        ImageProxy,
        List<FirebaseVisionImageLabel>>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedObject>) -> Unit,
            options: ObjectOptions = ObjectOptions()
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