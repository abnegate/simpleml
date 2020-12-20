package com.jakebarnby.simpleml.analyzer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jakebarnby.simpleml.Constants.ANALYZER_KEY
import com.jakebarnby.simpleml.camera2.view.Camera2Activity
import com.jakebarnby.simpleml.camera2.view.Camera2Fragment
import com.jakebarnby.simpleml.helpers.BindWrapper
import com.jakebarnby.simpleml.helpers.CoroutineBase
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
}