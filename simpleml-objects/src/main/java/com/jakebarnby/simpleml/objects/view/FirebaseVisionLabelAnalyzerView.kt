package com.jakebarnby.simpleml.objects.view

import android.content.Context
import android.util.AttributeSet
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.jakebarnby.simpleml.models.`object`.DetectedObject
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.FirebaseVisionLabelAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toFirebaseVisionImageLabelerRecognizerOptions
import com.jakebarnby.simpleml.objects.view.base.ObjectAnalyzerView

class FirebaseVisionLabelAnalyzerView : ObjectAnalyzerView<FirebaseVisionImageLabeler,
        FirebaseVisionCloudImageLabelerOptions,
        ImageProxy,
        List<FirebaseVisionImageLabel>,
        List<DetectedObject>> {

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

        setAnalyzer<FirebaseVisionLabelAnalyzer>((options as ObjectOptions)
            .toFirebaseVisionImageLabelerRecognizerOptions())
    }

    override fun setOnNextDetectionListener(onNext: (List<DetectedObject>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedObject() })
        }
    }
}