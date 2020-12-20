package com.jakebarnby.simpleml.text.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.DetectedText
import com.jakebarnby.simpleml.models.TextOptions
import com.jakebarnby.simpleml.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toTextRecognizerOptions

class LocalTextAnalyzerFragment : TextAnalyzerFragment<
        LocalTextAnalyzer,
        TextRecognizer,
        TextRecognizerOptions,
        ImageProxy,
        Text>()
{
    companion object {
        fun newInstance(
            onNextResult: (DetectedText) -> Unit,
            options: TextOptions = TextOptions()
        ) = LocalTextAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(LocalTextAnalyzer().apply {
                onAnalysisResult = {
                    onNextResult(it.toDetectedText())
                }
                initialize(options.toTextRecognizerOptions())
            }))
        }
    }
}