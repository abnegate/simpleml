package com.jakebarnby.camdroid.camera2.presenter

import com.jakebarnby.camdroid.analyzer.Analyzer
import com.jakebarnby.camdroid.camera2.Camera2Contract

class Camera2Presenter<TDetector, TOptions, TInput, TResult>(
    analyzerClass: Class<out Analyzer<TDetector, TOptions, TInput, TResult>>,
    options: TOptions,
    onNewResult: (TResult) -> Unit
): Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult> {

    override var view: Camera2Contract.View? = null
    override var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>? = null

    init {
        analyzer = analyzerClass.newInstance().apply {
            onAnalyzed = onNewResult
            initialize(options)
        }
    }

    override fun subscribe(view: Camera2Contract.View) {
        this.view = view
    }

    override fun unsubscribe() {
        this.view = null
    }
}