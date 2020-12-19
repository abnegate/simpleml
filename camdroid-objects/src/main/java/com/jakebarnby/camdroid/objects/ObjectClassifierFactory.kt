package com.jakebarnby.camdroid.objects

import android.app.Activity
import com.jakebarnby.camdroid.ImageClassifier
import com.jakebarnby.camdroid.classification.PhotoClassifier
import com.jakebarnby.camdroid.classification.ClassifierType
import com.jakebarnby.camdroid.objects.camera.FirebaseCloudClassifier
import com.jakebarnby.camdroid.objects.camera.TensorflowClassifier
import com.jakebarnby.camdroid.helpers.FileDownloader
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ObjectClassifierFactory : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private var job = Job()

    companion object {
        fun getFromConfiguration(
            activity: Activity,
            configuration: ImageClassifier.Configuration
        ) : PhotoClassifier = when (configuration.classifier) {
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