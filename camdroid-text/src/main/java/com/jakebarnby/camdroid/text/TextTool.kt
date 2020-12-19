package com.jakebarnby.camdroid.text

import android.app.Activity
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerTool
import com.jakebarnby.camdroid.models.*
import com.jakebarnby.camdroid.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.camdroid.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.camdroid.text.analyzer.RemoteTextAnalyzer
import kotlinx.coroutines.asExecutor
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
        val realOptions = TextRecognizerOptions.Builder()
            .setExecutor(options.detectionDispatcher.asExecutor())

        detectFromCamera<LocalTextAnalyzer, TextRecognizerOptions, Text>(
            context,
            realOptions.build(), { results ->
                onNextResult(DetectedText().apply {
                    text = results.text
                    textBoxes = results.textBlocks.map { block ->
                        TextBox().apply {
                            text = block.text
                            boundingBox = block.boundingBox
                            cornerPoints = block.cornerPoints?.toList()
                            detectedLanguages = listOf(block.recognizedLanguage)
                            lines = block.lines.map { line ->
                                TextLine().apply {
                                    text = line.text
                                    boundingBox = line.boundingBox
                                    cornerPoints = line.cornerPoints?.toList()
                                    detectedLanguages = listOf(block.recognizedLanguage)
                                    elements = line.elements.map { element ->
                                        TextElement().apply {
                                            text = element.text
                                            boundingBox = element.boundingBox
                                            cornerPoints = element.cornerPoints?.toList()
                                            detectedLanguages = listOf(block.recognizedLanguage)
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
        )
    }

    private fun detectTextsFirebaseVision(
        context: Activity,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions
    ) {
        val realOptions = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)

        detectFromCamera<RemoteTextAnalyzer, FirebaseVisionCloudTextRecognizerOptions, FirebaseVisionText>(
            context,
            realOptions.build(),
            { results ->
                onNextResult(
                    DetectedText().apply {
                        text = results.text
                        textBoxes = results.textBlocks.map { block ->
                            TextBox().apply {
                                text = block.text
                                confidence = block.confidence
                                boundingBox = block.boundingBox
                                cornerPoints = block.cornerPoints?.toList()
                                detectedLanguages = block.recognizedLanguages.map {
                                    it.languageCode ?: ""
                                }
                                lines = block.lines.map { line ->
                                    TextLine().apply {
                                        text = line.text
                                        confidence = line.confidence
                                        boundingBox = line.boundingBox
                                        cornerPoints = line.cornerPoints?.toList()
                                        detectedLanguages = block.recognizedLanguages.map {
                                            it.languageCode ?: ""
                                        }
                                        elements = line.elements.map { element ->
                                            TextElement().apply {
                                                text = element.text
                                                confidence = element.confidence
                                                boundingBox = element.boundingBox
                                                cornerPoints = element.cornerPoints?.toList()
                                                detectedLanguages = block.recognizedLanguages.map {
                                                    it.languageCode ?: ""
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        )
    }

    fun detectLanguages(
        context: Activity,
        text: String,
        onNextResult: (DetectedText) -> Unit,
        options: TextOptions = TextOptions()
    ) {
        val realOptions = LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(options.minimumConfidence)
            .setExecutor(options.detectionDispatcher.asExecutor())

        val analyzer = LocalLanguageAnalyzer().apply {
            onAnalysisResult = {
                onNextResult(DetectedText().apply {
                    this.text = text
                    this.detectedLanguages = it.map {
                        it.languageTag
                    }
                })
            }
        }

        analyzer.initialize(realOptions.build())

        launch {
            analyzer.analyzeInput(text)
        }
    }
}