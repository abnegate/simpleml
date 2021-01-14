package com.jakebarnby.simpleml.text.view

import android.content.Context
import android.util.AttributeSet
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.jakebarnby.simpleml.camera2.view.Camera2View
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.text.analyzer.LocalTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toTextRecognizerOptions

class LocalTextAnalyzerView : Camera2View<TextRecognizer,
    TextRecognizerOptions,
    ImageProxy,
    Text,
    DetectedText> {

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    override fun init(attrs: AttributeSet) {
        super.init(attrs)

        setAnalyzer<LocalTextAnalyzer>((options as TextOptions)
            .toTextRecognizerOptions())
    }

    override fun setOnNextDetectionListener(onNext: (DetectedText) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = {
            onNext(it.toDetectedText())
        }
    }
}