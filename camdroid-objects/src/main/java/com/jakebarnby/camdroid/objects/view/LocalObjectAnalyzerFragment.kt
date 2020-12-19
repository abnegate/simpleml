package com.jakebarnby.camdroid.objects.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.models.DetectedObject
import com.jakebarnby.camdroid.models.ObjectOptions
import com.jakebarnby.camdroid.objects.analyzer.LocalObjectAnalyzer
import com.jakebarnby.camdroid.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.camdroid.objects.extensions.ObjectOptionsExtensions.toObjectDetectorOptions

class LocalObjectAnalyzerFragment : Camera2Fragment<
        LocalObjectAnalyzer,
        ObjectDetector,
        ObjectDetectorOptions,
        ImageProxy,
        List<com.google.mlkit.vision.objects.DetectedObject>>()
{
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
}