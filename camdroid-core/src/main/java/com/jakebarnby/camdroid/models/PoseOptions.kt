package com.jakebarnby.camdroid.models

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PoseOptions(
    detectorMode: DetectorMode = DetectorMode.STREAM,
    detectionDispatcher: CoroutineDispatcher = Dispatchers.IO
) : OptionsBase(detectorMode, detectionDispatcher)