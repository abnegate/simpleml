package com.jakebarnby.samplesimpleml

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.objects.view.LocalObjectAnalyzerView
import kotlinx.coroutines.Job

class ViewActivity : AppCompatActivity(), CoroutineBase {

    override val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view)

        val cameraView = findViewById<LocalObjectAnalyzerView>(R.id.cameraView)

        cameraView.setOnNextDetectionListener { results ->
            Log.e(javaClass.name, results.joinToString {
                it.labels.joinToString { ", " }
            })
        }
    }
}
