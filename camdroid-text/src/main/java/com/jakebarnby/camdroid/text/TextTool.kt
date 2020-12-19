package com.jakebarnby.camdroid.text

import android.app.Activity
import android.content.Context
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.camdroid.analyzer.AnalyzerInteractor
import com.jakebarnby.camdroid.helpers.CoroutineBase
import com.jakebarnby.camdroid.models.AnalysisService
import com.jakebarnby.camdroid.models.DetectedText
import com.jakebarnby.camdroid.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.camdroid.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.camdroid.text.analyzer.RemoteTextAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch

class TextTool : AnalyzerInteractor(), CoroutineBase {

    fun detectTexts(
        context: Activity,
        onNextResult: (List<DetectedText>) -> Unit,
        analysisService: AnalysisService = AnalysisService.DEVICE
    ) = when (analysisService) {
        AnalysisService.DEVICE ->
            detectTextsOnDevice(context, onNextResult)
        AnalysisService.FIREBASE_VISION ->
            detectTextsFirebaseVision(context, onNextResult)
    }

    private fun detectTextsOnDevice(
        context: Activity,
        onNextResult: (List<DetectedText>) -> Unit
    ) {
        val options = TextRecognizerOptions.Builder()
            .setExecutor(Dispatchers.IO.asExecutor())

        startCamera(
            context,
            LocalTextAnalyzer::class.java,
            options.build(),
            onNextResult
        )
    }

    private fun detectTextsFirebaseVision(
        context: Activity,
        onNextResult: (List<DetectedText>) -> Unit
    ) {
        val options = TextRecognizerOptions.Builder()
            .setExecutor(Dispatchers.IO.asExecutor())

        startCamera(
            context,
            RemoteTextAnalyzer::class.java,
            options.build(),
            onNextResult
        )
    }

    fun detectLanguages(
        context: Activity,
        text: String,
        onNextResult: (List<DetectedText>) -> Unit,
        minimumConfidence: Float = 0.5f
    ) {
        val options = LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(minimumConfidence)
            .setExecutor(Dispatchers.IO.asExecutor())

        val analyzer = LocalLanguageAnalyzer().apply {
            onAnalyzed = {
                // TODO: Map language result
            }
        }

        analyzer.initialize(options.build())

        launch {
            analyzer.analyzeInput(text)
        }
    }
}