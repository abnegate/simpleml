package com.jakebarnby.samplesimpleml

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.objects.view.LocalObjectAnalyzerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

        launch {
            val uri = cameraView.takePicture("output_path.jpg")
            Log.e(javaClass.name, "Output uri is $uri")
        }
    }
}
