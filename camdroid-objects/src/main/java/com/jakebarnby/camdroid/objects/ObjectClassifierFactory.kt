package com.jakebarnby.camdroid.objects

import android.app.Activity
import com.jakebarnby.camdroid.Classification
import com.jakebarnby.camdroid.classification.Classifier
import com.jakebarnby.camdroid.classification.ClassifierType
import com.jakebarnby.camdroid.helpers.FileDownloader
import com.jakebarnby.camdroid.objects.camera.FirebaseCloudClassifier
import com.jakebarnby.camdroid.objects.camera.TensorflowClassifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class ObjectClassifierFactory : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private var job = Job()

    companion object {
        fun getFromConfiguration(
            activity: Activity,
            configuration: Classification.Configuration
        ): Classifier = when (configuration.classifier) {
            ClassifierType.FIREBASE_CLOUD -> FirebaseCloudClassifier(
                activity,
                null,
                configuration
            )
            ClassifierType.TENSORFLOW -> TensorflowClassifier(
                activity,
                configuration,
                FileDownloader()
            )
            else -> throw IllegalStateException("Not a valid classifier type")
        }
    }
}