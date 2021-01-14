package com.jakebarnby.simpleml.objects.view.base

import android.content.Context
import android.util.AttributeSet
import com.jakebarnby.simpleml.camera2.view.Camera2View
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.models.types.AnalysisMode
import com.jakebarnby.simpleml.objects.R

abstract class ObjectAnalyzerView<TDetector, TOptions, TInput, TResult, TOutResult>
    : Camera2View<TDetector, TOptions, TInput, TResult, TOutResult> {

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun init(attrs: AttributeSet) {
        super.init(attrs)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Camera2View,
            0, 0
        ).apply {
            try {
                val defaultOptions = ObjectOptions()
                val analysisMode = getInt(
                    R.styleable.Camera2View_simpleml_detectorMode,
                    defaultOptions.analysisMode.ordinal
                )
                val analysisDispatcher = getInt(
                    R.styleable.Camera2View_simpleml_detectionDispatcher,
                    defaultOptions.analysisDispatcher.ordinal
                )
                val analysisLocation = getInt(
                    R.styleable.Camera2View_simpleml_analysisLocation,
                    defaultOptions.analysisLocation.ordinal
                )
                val minimumConfidence = getFloat(
                    R.styleable.Camera2View_simpleml_minimumConfidence,
                    defaultOptions.minimumConfidence
                )
                val classificationEnabled = getBoolean(
                    R.styleable.Camera2View_simpleml_classificationEnabled,
                    defaultOptions.classificationEnabled
                )
                val detectMultiple = getBoolean(
                    R.styleable.Camera2View_simpleml_detectMultiple,
                    defaultOptions.detectMultiple
                )

                options = ObjectOptions(
                    minimumConfidence,
                    classificationEnabled,
                    detectMultiple,
                    AnalysisMode.values()[analysisMode],
                    AnalysisDispatcher.values()[analysisDispatcher],
                    AnalysisLocation.values()[analysisLocation],
                )
            } finally {
                recycle()
            }
        }
    }
}