package com.jakebarnby.simpleml.text.view

import android.content.Context
import android.util.AttributeSet
import com.google.mlkit.nl.languageid.IdentifiedLanguage
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.jakebarnby.simpleml.camera2.view.Camera2View
import com.jakebarnby.simpleml.models.text.DetectedLanguage
import com.jakebarnby.simpleml.models.text.TextOptions
import com.jakebarnby.simpleml.text.analyzer.LocalLanguageAnalyzer
import com.jakebarnby.simpleml.text.extensions.DetectedLanguageExtensions.toDetectedLanguage
import com.jakebarnby.simpleml.text.extensions.TextOptionsExtensions.toLanguageIdentificationOptions

class LocalLanguageAnalyzerView : Camera2View<
        LanguageIdentifier,
        LanguageIdentificationOptions,
        String,
        List<IdentifiedLanguage>,
        List<DetectedLanguage>> {

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

        setAnalyzer<LocalLanguageAnalyzer>(
            (options as TextOptions)
                .toLanguageIdentificationOptions()
        )
    }

    override fun setOnNextDetectionListener(onNext: (List<DetectedLanguage>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedLanguage() })
        }
    }
}