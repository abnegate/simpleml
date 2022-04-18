package com.jakebarnby.samplesimpleml

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jakebarnby.simpleml.helpers.CoroutineBase
import com.jakebarnby.simpleml.models.`object`.ObjectOptions
import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.models.types.AnalysisMode
import com.jakebarnby.simpleml.objects.fragment.base.ObjectAnalyzerFragment
import kotlinx.coroutines.Job

class FragmentActivity : AppCompatActivity(), CoroutineBase {

    override val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment)

        val objectAnalyzerFragment = ObjectAnalyzerFragment.newInstance(
            { detected ->
                Log.e(javaClass.name, detected.joinToString { obj ->
                    "${
                        obj.labels.joinToString {
                            "[LABEL] ${it.first}: [CONFIDENCE] ${it.second}\n"
                        }
                    }: [BOUNDS] ${obj.boundingBox}\n"
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

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.fragment_container_view, objectAnalyzerFragment)
            .commit()
    }
}
