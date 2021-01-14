package com.jakebarnby.simpleml.models.`object`

import android.graphics.Rect
import com.jakebarnby.simpleml.models.ResultBase

class DetectedObject(
    var labels: List<Pair<String, Float>> = listOf(),
    var boundingBox: Rect? = null
) : ResultBase() {

}