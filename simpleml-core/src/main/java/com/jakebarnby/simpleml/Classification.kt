package com.jakebarnby.simpleml

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jakebarnby.simpleml.analyzer.AnalyzerType
import com.jakebarnby.simpleml.camera2.view.Camera2Activity
import com.jakebarnby.simpleml.classification.ClassifierType
import java.io.Serializable

class Classification {

    companion object {
        private const val TAG = "ImageClassifier"
        private const val CONFIG_KEY = "classifier_data"

        fun start(
            activity: Activity,
            configuration: Configuration
        ) {
            checkConfiguration(configuration)
            moveToCameraActivity(activity, configuration)
        }

        private fun checkConfiguration(configuration: Configuration) {
            requireNotNull(configuration.classifier) { "Configuration must contain a classifier" }

            if (configuration.classifier == ClassifierType.TENSORFLOW
                && (configuration.modelUrl == null || configuration.labelUrl == null)
            ) {
                throw IllegalStateException("Tensorflow classifier requires model and labels urls (either local [assets/] or remote.")
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
