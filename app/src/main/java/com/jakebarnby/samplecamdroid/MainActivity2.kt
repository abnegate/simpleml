package com.jakebarnby.samplecamdroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.jakebarnby.camdroid.models.AnalysisLocation
import com.jakebarnby.camdroid.models.ObjectOptions
import com.jakebarnby.camdroid.models.PoseOptions
import com.jakebarnby.camdroid.objects.view.LocalObjectAnalyzerFragment
import com.jakebarnby.camdroid.poses.view.LocalPoseAnalyzerFragment

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        val objectDetectorFragment = LocalObjectAnalyzerFragment.newInstance({ results ->
            Log.e(javaClass.name, results.joinToString {
                "${it.labels}: ${it.boundingBox}\n"
            })
        })

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.fragment_container_view, objectDetectorFragment)
            .commit()
    }
}
