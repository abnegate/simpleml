package com.jakebarnby.simpleml.text.fragment

import androidx.camera.core.ImageProxy
import androidx.core.os.bundleOf
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.jakebarnby.simpleml.Constants
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.text.analyzer.FirebaseVisionTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toFirebaseVisionTextRecognizerOptions
import com.jakebarnby.simpleml.text.fragment.base.TextAnalyzerFragment

class FirebaseVisionTextAnalyzerFragment : TextAnalyzerFragment<
        FirebaseVisionTextAnalyzer,
        FirebaseVisionTextRecognizer,
        FirebaseVisionCloudTextRecognizerOptions,
        ImageProxy,
        FirebaseVisionText,
        DetectedText>() {
    companion object {
        fun newInstance(
            onNextResult: (DetectedText) -> Unit,
            options: TextOptions = TextOptions()
        ) = FirebaseVisionTextAnalyzerFragment().apply {
            arguments =
                bundleOf(Constants.ANALYZER_KEY to BindWrapper(FirebaseVisionTextAnalyzer().apply {
                    onAnalysisResult = {
                        onNextResult(it.toDetectedText())
                    }
                    initialize(options.toFirebaseVisionTextRecognizerOptions())
                }))
        }
    }

    override fun setOnNextDetectionListener(onNext: (DetectedText) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = {
            onNext(it.toDetectedText())
        }
    }
}