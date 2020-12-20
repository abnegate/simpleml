package com.jakebarnby.simpleml.models

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.Serializable

open class OptionsBase(
    val detectorMode: DetectorMode = DetectorMode.STREAM,
    val detectionDispatcher: CoroutineDispatcher = Dispatchers.IO,
    val analysisLocation: AnalysisLocation = AnalysisLocation.DEVICE
) : Serializable