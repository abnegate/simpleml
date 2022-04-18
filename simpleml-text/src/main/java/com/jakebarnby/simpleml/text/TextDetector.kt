package com.jakebarnby.simpleml.text

import android.app.Activity
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jakebarnby.simpleml.analyzer.Detector
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.simpleml.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toLanguageIdentificationOptions
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toTextRecognizerOptions
import kotlinx.coroutines.launch

class TextDetector : Detector() {

    fun stream(
        context: Activity,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions = TextOptions()
    ) = when (options.analysisLocation) {
        AnalysisLocation.DEVICE ->
            detectTextsOnDevice(context, onNextResult, options)
    }

    private fun detectTextsOnDevice(
        context: Activity,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions
    ) {
        startDetectorActivity<LocalTextAnalyzer, TextRecognizer, TextRecognizerOptions, ImageProxy, Text>(
            context,
            options.toTextRecognizerOptions()
        ) {
            onNextResult(it.toDetectedText())
        }
    }

    fun detectLanguages(
        text: String,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions = TextOptions()
    ) {
        val analyzer = LocalLanguageAnalyzer().apply {
            onAnalysisResult = {
                onNextResult(it.toDetectedText(text))
            }
        }

        analyzer.initialize(options.toLanguageIdentificationOptions())

        launch {
            analyzer.analyzeInput(text)
        }
    }
}
