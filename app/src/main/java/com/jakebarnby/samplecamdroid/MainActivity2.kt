package com.jakebarnby.samplecamdroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.jakebarnby.camdroid.models.PoseOptions
import com.jakebarnby.camdroid.poses.view.LocalPoseAnalyzerFragment

class MainActivity2 : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val poseDetectorFragment = LocalPoseAnalyzerFragment.newInstance(PoseOptions()) { results ->
            Log.e(javaClass.name, results.joinToString {
                "${it.landmark}: ${it.position}, in frame likelihood: ${it.inFrameLikelihood}"
            })
        }
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.fragment_container_view, poseDetectorFragment)
            .commit()
    }
}
