package com.jakebarnby.simpleml.objects.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.DetectedObject
import com.jakebarnby.simpleml.models.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.LocalLabelAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toImageLabelerOptions

class LocalLabelAnalyzerFragment : Camera2Fragment<
        LocalLabelAnalyzer,
        ImageLabeler,
        ImageLabelerOptions,
        ImageProxy,
        List<ImageLabel>>() {
    companion object {
        fun newInstance(
            onNextResult: (List<DetectedObject>) -> Unit,
            options: ObjectOptions = ObjectOptions()
        ) = LocalLabelAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(LocalLabelAnalyzer().apply {
                onAnalysisResult = { results ->
                    onNextResult(results.map { it.toDetectedObject() })
                }
                initialize(options.toImageLabelerOptions())
            }))
        }
    }
}