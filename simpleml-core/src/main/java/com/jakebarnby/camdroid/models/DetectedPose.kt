package com.jakebarnby.simpleml.models

import android.graphics.PointF

class DetectedPose(
    var position: PointF? = null,
    var landmark: PoseLandmark? = null,
    var inFrameLikelihood: Float? = null
) : ResultBase()