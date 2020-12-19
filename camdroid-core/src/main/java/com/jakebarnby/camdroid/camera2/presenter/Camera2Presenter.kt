package com.jakebarnby.camdroid.camera2.presenter

import com.jakebarnby.camdroid.analyzer.Analyzer
import com.jakebarnby.camdroid.camera2.Camera2Contract

class Camera2Presenter<TDetector, TOptions, TInput, TResult>(
    override var analyzer: Analyzer<TDetector, TOptions, TInput, TResult>,
) : Camera2Contract.Presenter<TDetector, TOptions, TInput, TResult> {

    override var view: Camera2Contract.View? = null

    override fun subscribe(view: Camera2Contract.View) {
        this.view = view
    }

    override fun unsubscribe() {
        this.view = null
    }
}