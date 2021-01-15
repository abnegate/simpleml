package com.jakebarnby.simpleml.models.pose

import com.jakebarnby.simpleml.models.OptionsBase
import com.jakebarnby.simpleml.models.types.AnalysisDispatcher
import com.jakebarnby.simpleml.models.types.AnalysisLocation
import com.jakebarnby.simpleml.models.types.AnalysisMode
import com.jakebarnby.simpleml.models.types.AnalysisType

class PoseOptions(
    analysisMode: AnalysisMode = AnalysisMode.FRAME_STREAM,
    analysisDispatcher: AnalysisDispatcher = AnalysisDispatcher.IO,
    analysisLocation: AnalysisLocation = AnalysisLocation.DEVICE
) : OptionsBase(
    AnalysisType.POSE,
    analysisMode,
    analysisDispatcher,
    analysisLocation
)