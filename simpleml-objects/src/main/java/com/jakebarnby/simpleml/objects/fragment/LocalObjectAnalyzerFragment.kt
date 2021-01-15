package com.jakebarnby.simpleml.objects.fragment

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.`object`.DetectedObject
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.LocalObjectAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toObjectDetectorOptions
import com.jakebarnby.simpleml.objects.fragment.base.ObjectAnalyzerFragment

class LocalObjectAnalyzerFragment : ObjectAnalyzerFragment<
        LocalObjectAnalyzer,
        ObjectDetector,
        ObjectDetectorOptions,
        ImageProxy,
        List<com.google.mlkit.vision.objects.DetectedObject>,
        List<DetectedObject>>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedObject>) -> Unit,
            options: ObjectOptions = ObjectOptions()
        ) = LocalObjectAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(LocalObjectAnalyzer().apply {
                onAnalysisResult = { results ->
                    onNextResult(results.map { it.toDetectedObject() })
                }
                initialize(options.toObjectDetectorOptions())
            }))
        }
    }

    override fun setOnNextDetectionListener(onNext: (List<DetectedObject>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedObject() })
        }
    }
}