package com.jakebarnby.camdroid.text

import android.app.Activity
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerTool
import com.jakebarnby.camdroid.models.AnalysisLocation
import com.jakebarnby.camdroid.models.DetectedText
import com.jakebarnby.camdroid.models.TextOptions
import com.jakebarnby.camdroid.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.camdroid.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.camdroid.text.analyzer.RemoteTextAnalyzer
import com.jakebarnby.camdroid.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.camdroid.text.extensions.TextOptionsExtensions.toFirebaseVisionTextRecognizerOptions
import com.jakebarnby.camdroid.text.extensions.TextOptionsExtensions.toLanguageIdentificationOptions
import com.jakebarnby.camdroid.text.extensions.TextOptionsExtensions.toTextRecognizerOptions
import kotlinx.coroutines.launch

class TextTool : AnalyzerTool() {

    fun detectTexts(
        context: Activity,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions = TextOptions()
    ) = when (options.analysisLocation) {
        AnalysisLocation.DEVICE ->
            detectTextsOnDevice(context, onNextResult, options)
        AnalysisLocation.FIREBASE_VISION ->
            detectTextsFirebaseVision(context, onNextResult, options)
    }

    private fun detectTextsOnDevice(
        context: Activity,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions
    ) {
        startCameraActivity<LocalTextAnalyzer, TextRecognizer, TextRecognizerOptions, ImageProxy, Text>(
            context,
            options.toTextRecognizerOptions(), {
                onNextResult(it.toDetectedText())
            }
        )
    }

    private fun detectTextsFirebaseVision(
        context: Activity,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions
    ) {
        startCameraActivity<RemoteTextAnalyzer, FirebaseVisionTextRecognizer, FirebaseVisionCloudTextRecognizerOptions, ImageProxy, FirebaseVisionText>(
            context,
            options.toFirebaseVisionTextRecognizerOptions(), {
                onNextResult(it.toDetectedText())
            }
        )
    }

    fun detectLanguages(
        context: Activity,
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