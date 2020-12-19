package com.jakebarnby.camdroid.models

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TextOptions(
    val minimumConfidence: Float = 0.5f,
    detectorMode: DetectorMode = DetectorMode.STREAM,
    detectionDispatcher: CoroutineDispatcher = Dispatchers.IO
) : OptionsBase(detectorMode, detectionDispatcher)