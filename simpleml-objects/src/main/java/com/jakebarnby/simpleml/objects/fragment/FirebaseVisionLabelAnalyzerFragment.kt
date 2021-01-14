package com.jakebarnby.simpleml.objects.fragment

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.`object`.DetectedObject
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.FirebaseVisionLabelAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toFirebaseVisionImageLabelerRecognizerOptions
import com.jakebarnby.simpleml.objects.fragment.base.ObjectAnalyzerFragment

class FirebaseVisionLabelAnalyzerFragment : ObjectAnalyzerFragment<
        FirebaseVisionLabelAnalyzer,
        FirebaseVisionImageLabeler,
        FirebaseVisionCloudImageLabelerOptions,
        ImageProxy,
        List<FirebaseVisionImageLabel>,
        List<DetectedObject>>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedObject>) -> Unit,
            options: ObjectOptions = ObjectOptions()
        ) = FirebaseVisionLabelAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(FirebaseVisionLabelAnalyzer().apply {
                onAnalysisResult = { results ->
                    onNextResult(results.map { it.toDetectedObject() })
                }
                initialize(options.toFirebaseVisionImageLabelerRecognizerOptions())
            }))
        }
    }

    override fun setOnNextDetectionListener(onNext: (List<DetectedObject>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedObject() })
        }
    }
}