package com.jakebarnby.samplecamdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.jakebarnby.camdroid.ImageClassifier
import com.jakebarnby.camdroid.classification.ClassifierType
import com.jakebarnby.camdroid.objects.ObjectTool
import com.jakebarnby.camdroid.poses.PoseTool
import com.jakebarnby.camdroid.text.TextTool

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    startV2ObjectDetector()
  }

  fun startV1Detector() {
    ImageClassifier.Builder(this)
      .setClassifierType(ClassifierType.TENSORFLOW)
      .setModelUrl("https://github.com/googlecodelabs/tensorflow-for-poets-2/raw/master/android/tflite/app/src/main/assets/graph.lite")
      //.setModelUrl("graph.lite") Use included assets
      .setLabelUrl("labels.txt")
      .setMinimumConfidence(0.7f)
      .start()
  }

  private fun startV2ObjectDetector() {
    val tool = ObjectTool()

    tool.detectObjects(this, { results ->
      for (result in results) {
        Log.i(javaClass.name, result.toString())
      }
    })
  }

  private fun startV2PoseDetector() {
    val tool = PoseTool()

    tool.detectPoses(this) { results ->
      for (result in results) {
        Log.i(javaClass.name, result.toString())
      }
    }
  }

  private fun startV2TextDetector() {
    val tool = TextTool()

    tool.detectTexts(this, { results ->
      for (result in results) {
        Log.i(javaClass.name, result.toString())
      }
    })
  }
}
