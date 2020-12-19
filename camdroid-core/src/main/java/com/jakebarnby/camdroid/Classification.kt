package com.jakebarnby.camdroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.jakebarnby.camdroid.analyzer.AnalyzerType
import com.jakebarnby.camdroid.camera2.view.Camera2Activity
import com.jakebarnby.camdroid.classification.ClassifierType
import java.io.Serializable

class Classification {

    companion object {
        private const val TAG = "ImageClassifier"
        const val CONFIG_KEY = "classifier_data"

        fun start(
            activity: Activity,
            configuration: Configuration
        ) {
            checkConfiguration(configuration)
            moveToCameraActivity(activity, configuration)
        }

        private fun checkConfiguration(configuration: Configuration) {
            if (configuration.classifier == null) {
                throw IllegalStateException("ImageClassifier requires a [ClassifierType] to be set.")
            }
            if (configuration.classifier == ClassifierType.TENSORFLOW
                && configuration.modelUrl == null
            ) {
                throw IllegalStateException("Tensorflow classifier requires a model url (either local [assets/] or remote.")
            }
            if (configuration.classifier == ClassifierType.TENSORFLOW
                && configuration.labelUrl == null
            ) {
                throw IllegalStateException("Tensorflow classifier requires a label url (either local [assets/] or remote.")
            }
            if (configuration.minimumConfidence == 0f) {
                Log.w(
                    TAG,
                    "ImageClassifier created with 0 minimum confidence, erroneous results may be shown."
                )
            }
        }

        private fun moveToCameraActivity(activity: Activity, configuration: Configuration) {
            val intent = Intent(activity, Camera2Activity::class.java).apply {
                val bundle = Bundle().apply {
                    putSerializable(CONFIG_KEY, configuration)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }

    data class Configuration(
        var analyzerType: AnalyzerType? = null,
        var classifier: ClassifierType? = null,
        var modelUrl: String? = null,
        var labelUrl: String? = null,
        var minimumConfidence: Float? = 0f
    ) : Serializable
}
