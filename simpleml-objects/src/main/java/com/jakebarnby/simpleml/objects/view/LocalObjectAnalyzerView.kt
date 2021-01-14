package com.jakebarnby.simpleml.objects.view

import android.content.Context
import android.util.AttributeSet
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.objects.analyzer.LocalObjectAnalyzer
import com.jakebarnby.simpleml.objects.extensions.DetectedObjectExtensions.toDetectedObject
import com.jakebarnby.simpleml.objects.extensions.ObjectOptionsExtensions.toObjectDetectorOptions
import com.jakebarnby.simpleml.objects.view.base.ObjectAnalyzerView

class LocalObjectAnalyzerView : ObjectAnalyzerView<ObjectDetector,
        ObjectDetectorOptions,
        ImageProxy,
        List<DetectedObject>,
        List<com.jakebarnby.simpleml.models.`object`.DetectedObject>> {

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

        setAnalyzer<LocalObjectAnalyzer>(
            (options as ObjectOptions)
                .toObjectDetectorOptions()
        )
    }

    override fun setOnNextDetectionListener(onNext: (List<com.jakebarnby.simpleml.models.`object`.DetectedObject>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedObject() })
        }
    }
}