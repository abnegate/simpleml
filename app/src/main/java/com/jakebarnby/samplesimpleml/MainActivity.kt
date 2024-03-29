package com.jakebarnby.samplesimpleml

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.simpleml.Classification
import com.jakebarnby.simpleml.classification.ClassifierType
import com.jakebarnby.simpleml.objects.ObjectDetector
import com.jakebarnby.simpleml.poses.PoseDetector
import com.jakebarnby.simpleml.text.TextDetector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnObject).setOnClickListener {
            startObjectDetector()
        }
        findViewById<Button>(R.id.btnText).setOnClickListener {
            startTextDetector()
        }
        findViewById<Button>(R.id.btnPose).setOnClickListener {
            startPoseDetector()
        }
        findViewById<Button>(R.id.btnFragment).setOnClickListener {
            goToFragment()
        }
        findViewById<Button>(R.id.btnView).setOnClickListener {
            goToView()
        }
        findViewById<Button>(R.id.btnTensorflow).setOnClickListener {
            startClassifier()
        }
    }

    fun startClassifier() {
        val configuration = Classification.Configuration(
            classifier = ClassifierType.TENSORFLOW,
            modelUrl = "https://github.com/googlecodelabs/tensorflow-for-poets-2/raw/master/android/tflite/app/src/main/assets/graph.lite",
            labelUrl = "labels.txt",
            minimumConfidence = 0.5f
        )
        Classification.start(
            this,
            configuration
        )
    }

    private fun startObjectDetector() {
        ObjectDetector().stream(this, { results ->
            for (result in results) {
                Log.i(javaClass.name, result.toString())
            }
        })
    }

    private fun startPoseDetector() {
        PoseDetector().stream(this, { results ->
            for (result in results) {
                Log.i(javaClass.name, results.joinToString {
                    "${it.landmark}: ${it.position}, in frame: ${it.inFrameLikelihood}\n"
                })
            }
        })
    }

    private fun startTextDetector() {
        TextDetector().stream(this, { result ->
            Log.i(javaClass.name, result.toString())
        })
    }

    private fun goToFragment() {
        startActivity(Intent(this, FragmentActivity::class.java))
    }

    private fun goToView() {
        startActivity(Intent(this, ViewActivity::class.java))
    }
}
