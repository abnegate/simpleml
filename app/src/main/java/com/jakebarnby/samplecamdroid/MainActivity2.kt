package com.jakebarnby.samplesimpleml

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.simpleml.objects.view.ObjectAnalyzerFragment
import com.jakebarnby.simpleml.poses.view.PoseAnalyzerFragment
import com.jakebarnby.simpleml.text.view.TextAnalyzerFragment

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        val objectAnalyzerFragment = ObjectAnalyzerFragment.newInstance({ results ->
            Log.e(javaClass.name, results.joinToString {
                "${it.labels}: ${it.boundingBox}\n"
            })
        })

        val poseAnalyzerFramgnet = PoseAnalyzerFragment.newInstance({

        })

        val textAnalyzerFragment = TextAnalyzerFragment.newInstance({ results ->

        })

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.fragment_container_view1, objectAnalyzerFragment)
            .commit()
    }
}
