package com.jakebarnby.simpleml.models.pose

import android.graphics.PointF
import com.jakebarnby.simpleml.models.ResultBase

class DetectedPose(
    var position: PointF? = null,
    var landmark: PoseLandmark? = null,
    var inFrameLikelihood: Float? = null
) : ResultBase()