package com.jakebarnby.simpleml.text.view

import android.content.Context
import android.util.AttributeSet
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.jakebarnby.simpleml.camera2.view.Camera2View
import com.jakebarnby.simpleml.models.text.DetectedText
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.text.analyzer.FirebaseVisionTextAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedTextExtensions.toDetectedText
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toFirebaseVisionTextRecognizerOptions

class FirebaseVisionTextAnalyzerView : Camera2View<FirebaseVisionTextRecognizer,
        FirebaseVisionCloudTextRecognizerOptions,
        ImageProxy,
        FirebaseVisionText,
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

        setAnalyzer<FirebaseVisionTextAnalyzer>(
            (options as TextOptions)
                .toFirebaseVisionTextRecognizerOptions()
        )
    }

    override fun setOnNextDetectionListener(onNext: (DetectedText) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = {
            onNext(it.toDetectedText())
        }
    }
}