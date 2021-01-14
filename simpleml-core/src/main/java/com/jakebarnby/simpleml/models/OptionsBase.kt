package com.jakebarnby.simpleml.models

import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.models.types.AnalysisMode
import com.jakebarnby.simpleml.models.types.AnalysisType
import java.io.Serializable

open class OptionsBase(
    val analysisType: AnalysisType,
    val analysisMode: AnalysisMode = AnalysisMode.FRAME_STREAM,
    val analysisDispatcher: AnalysisDispatcher = AnalysisDispatcher.IO,
    val analysisLocation: AnalysisLocation = AnalysisLocation.DEVICE
) : Serializable