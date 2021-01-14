package com.jakebarnby.simpleml.objects.view

import android.content.Context
import android.util.AttributeSet
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.jakebarnby.simpleml.models.`object`.DetectedObject
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.LocalLabelAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toImageLabelerOptions
import com.jakebarnby.simpleml.objects.view.base.LabelAnalyzerView

class LocalLabelAnalyzerView : LabelAnalyzerView<
        ImageLabeler,
        ImageLabelerOptions,
        ImageProxy,
        List<ImageLabel>,
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

        setAnalyzer<LocalLabelAnalyzer>((options as ObjectOptions)
            .toImageLabelerOptions())
    }

    override fun setOnNextDetectionListener(onNext: (List<DetectedObject>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedObject() })
        }
    }
}