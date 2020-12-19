package com.jakebarnby.camdroid.analyzer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakebarnby.camdroid.Constants
import com.jakebarnby.camdroid.camera2.view.Camera2Activity
import java.io.Serializable

abstract class AnalyzerInteractor {

    protected fun <TOptions, TResult> startCamera(
        context: Activity,
        clazz: Class<*>,
        options: TOptions,
        onNextResult: (TResult) -> Unit
    ) {
        val intent = Intent(context, Camera2Activity::class.java).apply {
            val bundle = Bundle().apply {
                putSerializable(Constants.ANALYZER_CLASS_KEY, clazz)
                // TODO: Proxy options all the way to detectors to force serializable through here
                //putSerializable(Constants.ANALYZER_OPTIONS_KEY, options as Serializable)
                putSerializable(Constants.ANALYZER_RESULT_FUNCTION_KEY, onNextResult as Serializable)
            }
            putExtras(bundle)
        }
        context.startActivity(intent)
    }
}