package com.jakebarnby.camdroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.jakebarnby.camdroid.camera2.view.Camera2Activity
import com.jakebarnby.camdroid.classification.ClassifierType
import java.io.Serializable

class ImageClassifier {

    companion object {
        const val TAG = "ImageClassifier"
        const val CONFIG_KEY = "classifier_data"
    }

    data class Configuration(
        var classifier: ClassifierType? = null,
        var modelUrl: String? = null,
        var labelUrl: String? = null,
        var minimumConfidence: Float? = 0f
    ) : Serializable

    class Builder(var activity: Activity) {

        private val config = Configuration()

        fun setClassifierType(classifier: ClassifierType): Builder {
            config.classifier = classifier
            return this
        }

        fun setModelUrl(url: String): Builder {
            config.modelUrl = url
            return this
        }

        fun setLabelUrl(url: String): Builder {
            config.labelUrl = url
            return this
        }

        fun setMinimumConfidence(minConfidence: Float): Builder {
            config.minimumConfidence = minConfidence
            return this
        }

        fun start() {
            checkConfig()
            moveToCameraActivity()
        }

        private fun checkConfig() {
            if (config.classifier == null) {
                throw IllegalStateException("ImageClassifier requires a [ClassifierType] to be set.")
            }
            if (config.classifier == ClassifierType.TENSORFLOW
                && config.modelUrl == null
            ) {
                throw IllegalStateException("Tensorflow classifier requires a model url (either local [assets/] or remote.")
            }
            if (config.classifier == ClassifierType.TENSORFLOW
                && config.labelUrl == null
            ) {
                throw IllegalStateException("Tensorflow classifier requires a label url (either local [assets/] or remote.")
            }
            if (config.minimumConfidence == 0f) {
                Log.w(TAG, "ImageClassifier created with 0 minimum confidence, erroneous results may be shown.")
            }
        }

        private fun moveToCameraActivity() {
            val intent = Intent(activity, Camera2Activity::class.java).apply {
                val bundle = Bundle().apply {
                    putSerializable(CONFIG_KEY, config)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }
}
