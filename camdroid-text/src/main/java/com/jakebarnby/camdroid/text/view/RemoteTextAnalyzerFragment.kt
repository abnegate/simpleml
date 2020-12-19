package com.jakebarnby.camdroid.text.view

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.models.DetectedText
import com.jakebarnby.camdroid.models.TextOptions
import com.jakebarnby.camdroid.text.analyzer.RemoteTextAnalyzer
import com.jakebarnby.camdroid.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.camdroid.text.extensions.TextOptionsExtensions.toFirebaseVisionTextRecognizerOptions

class RemoteTextAnalyzerFragment :
    Camera2Fragment<RemoteTextAnalyzer, FirebaseVisionTextRecognizer, FirebaseVisionCloudTextRecognizerOptions, ImageProxy, FirebaseVisionText>() {
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