package com.jakebarnby.camdroid.analyzer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jakebarnby.camdroid.Constants.ANALYZER_KEY
import com.jakebarnby.camdroid.camera2.view.Camera2Activity
import com.jakebarnby.camdroid.camera2.view.Camera2Fragment
import com.jakebarnby.camdroid.helpers.BindWrapper
import com.jakebarnby.camdroid.helpers.CoroutineBase
import kotlinx.coroutines.Job

abstract class AnalyzerTool : CoroutineBase {

    override val job = Job()

    protected inline fun <reified TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
            TDetector,
            TOptions,
            TInput,
            TResult> startCameraActivity(
        context: Activity,
        options: TOptions,
        noinline onNextResult: (TResult) -> Unit
    ) {
        val analyzer = TAnalyzer::class.java.newInstance().apply {
            onAnalysisResult = onNextResult
            initialize(options)
        }
        val bundle = Bundle().apply {
            putBinder(ANALYZER_KEY, BindWrapper(analyzer))
        }
        val intent = Intent(context, Camera2Activity::class.java).apply {
            putExtras(bundle)
        }
        context.startActivity(intent)
    }

    companion object {
        inline fun <reified TAnalyzer : Analyzer<TDetector, TOptions, TInput, TResult>,
                TDetector,
                TOptions,
                TInput,
                TResult> getCameraFragment(
            options: TOptions,
            noinline onNextResult: (TResult) -> Unit
        ): Camera2Fragment<TAnalyzer, TDetector, TOptions, TInput, TResult> {
            val analyzer = TAnalyzer::class.java.newInstance().apply {
                onAnalysisResult = onNextResult
                initialize(options)
            }
            return Camera2Fragment.newInstance(analyzer)
        }
    }
}