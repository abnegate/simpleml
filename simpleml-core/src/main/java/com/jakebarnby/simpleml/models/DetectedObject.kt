package com.jakebarnby.simpleml.models

import android.graphics.Rect

class DetectedObject(
    var labels: List<Pair<String, Float>> = listOf(),
    var boundingBox: Rect? = null
) : ResultBase() {

}