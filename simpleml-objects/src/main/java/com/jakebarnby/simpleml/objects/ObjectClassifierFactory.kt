package com.jakebarnby.simpleml.objects

import android.app.Activity
import com.jakebarnby.simpleml.Classification
import com.jakebarnby.simpleml.classification.Classifier
import com.jakebarnby.simpleml.classification.ClassifierType
import com.jakebarnby.simpleml.helpers.FileDownloader
import com.jakebarnby.simpleml.objects.classifier.TensorflowClassifier
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
            ClassifierType.TENSORFLOW -> TensorflowClassifier(
                activity,
                configuration,
                FileDownloader()
            )
            else -> throw IllegalStateException("Not a valid classifier type")
        }
    }
}