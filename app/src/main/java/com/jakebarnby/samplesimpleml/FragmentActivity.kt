package com.jakebarnby.samplesimpleml

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.models.`object`.DetectedObject
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.models.types.AnalysisMode
import com.jakebarnby.simpleml.objects.fragment.LocalObjectAnalyzerFragment
import com.jakebarnby.simpleml.objects.fragment.base.ObjectAnalyzerFragment
import com.jakebarnby.simpleml.poses.fragment.PoseAnalyzerFragment
import com.jakebarnby.simpleml.text.fragment.base.TextAnalyzerFragment
import kotlinx.coroutines.Job

class FragmentActivity : AppCompatActivity(), CoroutineBase {

    override val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment)

        val objectAnalyzerFragment = ObjectAnalyzerFragment.newInstance(
            { results ->
                Log.e(javaClass.name, results.joinToString {
                    "${it.labels}: ${it.boundingBox}\n"
                })
            }, ObjectOptions(
                minimumConfidence = 0.7f,
                classificationEnabled = true,
                detectMultiple = true,
                analysisMode = AnalysisMode.FRAME_STREAM,
                analysisDispatcher = AnalysisDispatcher.IO,
                analysisLocation = AnalysisLocation.DEVICE
            )
        )

        val poseAnalyzerFramgnet = PoseAnalyzerFragment.newInstance({ results ->
            Log.e(javaClass.name, results.joinToString {
                "${it.landmark.toString()}: ${it.position}\n"
            })
        })

        val textAnalyzerFragment = TextAnalyzerFragment.newInstance({ result ->
            Log.e(javaClass.name, result.text ?: "")
        })

//        supportFragmentManager.beginTransaction()
//            .setReorderingAllowed(true)
//            .add(R.id.fragment_container_view, objectAnalyzerFragment)
//            .commit()

        val fragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as LocalObjectAnalyzerFragment

        fragment.setOnNextDetectionListener { results: List<DetectedObject> ->
            Log.e(javaClass.name, results.joinToString {
                "${it.labels}: ${it.boundingBox}\n"
            })
        }
    }
}
