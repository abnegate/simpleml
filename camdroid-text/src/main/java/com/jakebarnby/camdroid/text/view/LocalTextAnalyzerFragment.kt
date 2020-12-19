package com.jakebarnby.camdroid.text.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.models.DetectedText
import com.jakebarnby.camdroid.models.TextOptions
import com.jakebarnby.camdroid.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.camdroid.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.camdroid.text.extensions.TextOptionsExtensions.toTextRecognizerOptions

class LocalTextAnalyzerFragment :
    Camera2Fragment<LocalTextAnalyzer, TextRecognizer, TextRecognizerOptions, ImageProxy, Text>() {
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