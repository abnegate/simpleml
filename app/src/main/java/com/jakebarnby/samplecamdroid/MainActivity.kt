package com.jakebarnby.samplecamdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.camdroid.Classification
import com.jakebarnby.camdroid.classification.ClassifierType
import com.jakebarnby.camdroid.objects.ObjectTool
import com.jakebarnby.camdroid.poses.PoseTool
import com.jakebarnby.camdroid.text.TextTool

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
        ObjectTool().detectObjects(this, { results ->
            for (result in results) {
                Log.i(javaClass.name, result.toString())
            }
        })
    }

    private fun startPoseDetector() {
        PoseTool().detectPoses(this, { results ->
            for (result in results) {
                Log.i(javaClass.name, result.toString())
            }
        })
    }

    private fun startTextDetector() {
        TextTool().detectTexts(this, { result ->
            Log.i(javaClass.name, result.toString())
        })
    }

    private fun goToFragment() {
        startActivity(
            Intent(this, MainActivity2::class.java)
        )
    }
}
