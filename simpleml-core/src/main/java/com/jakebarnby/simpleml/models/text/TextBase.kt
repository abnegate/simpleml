package com.jakebarnby.simpleml.models.text

import android.graphics.Point
import android.graphics.Rect
import com.jakebarnby.simpleml.models.Positionable

open class TextBase(
    var text: String? = null,
    var detectedLanguages: List<String>? = null,
    var confidence: Float? = null,
    override var boundingBox: Rect? = null,
    override var cornerPoints: List<Point>? = null
) : Positionable