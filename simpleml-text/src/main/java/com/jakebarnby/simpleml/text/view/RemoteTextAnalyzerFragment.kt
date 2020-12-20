package com.jakebarnby.simpleml.text.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.DetectedText
import com.jakebarnby.simpleml.models.TextOptions
import com.jakebarnby.simpleml.text.analyzer.RemoteTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toFirebaseVisionTextRecognizerOptions

class RemoteTextAnalyzerFragment : TextAnalyzerFragment<
        RemoteTextAnalyzer,
        FirebaseVisionTextRecognizer,
        FirebaseVisionCloudTextRecognizerOptions,
        ImageProxy,
        FirebaseVisionText>()
{
    companion object {
        fun newInstance(
            onNextResult: (DetectedText) -> Unit,
            options: TextOptions = TextOptions()
        ) = RemoteTextAnalyzerFragment().apply {
            arguments = bundleOf(Constants.ANALYZER_KEY to BindWrapper(RemoteTextAnalyzer().apply {
                onAnalysisResult = {
                    onNextResult(it.toDetectedText())
                }
                initialize(options.toFirebaseVisionTextRecognizerOptions())
            }))
        }
    }
}