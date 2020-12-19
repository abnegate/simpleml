package com.jakebarnby.camdroid.models

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ObjectOptions(
    val minimumConfidence: Float = 0.5f,
    val classificationEnabled: Boolean = true,
    val detectMultiple: Boolean = true,
    detectorMode: DetectorMode = DetectorMode.STREAM,
    detectionDispatcher: CoroutineDispatcher = Dispatchers.IO
) : OptionsBase(detectorMode, detectionDispatcher)