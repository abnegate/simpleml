package com.jakebarnby.simpleml.text.fragment

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toTextRecognizerOptions
import com.jakebarnby.simpleml.text.fragment.base.TextAnalyzerFragment

class LocalTextAnalyzerFragment : TextAnalyzerFragment<
        LocalTextAnalyzer,
        TextRecognizer,
        TextRecognizerOptions,
        ImageProxy,
        Text,
        DetectedText>() {
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

    override fun setOnNextDetectionListener(onNext: (DetectedText) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = {
            onNext(it.toDetectedText())
        }
    }
}