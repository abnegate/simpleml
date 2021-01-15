package com.jakebarnby.simpleml.models.`object`

import com.jakebarnby.simpleml.models.OptionsBase
import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.models.types.AnalysisMode
import com.jakebarnby.simpleml.models.types.AnalysisType

class ObjectOptions(
    val minimumConfidence: Float = 0.5f,
    val classificationEnabled: Boolean = true,
    val detectMultiple: Boolean = true,
    analysisMode: AnalysisMode = AnalysisMode.FRAME_STREAM,
    analysisDispatcher: AnalysisDispatcher = AnalysisDispatcher.IO,
    analysisLocation: AnalysisLocation = AnalysisLocation.DEVICE
) : OptionsBase(
    AnalysisType.OBJECT,
    analysisMode,
    analysisDispatcher,
    analysisLocation
)