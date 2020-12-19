package com.jakebarnby.camdroid.models

import android.graphics.Point
import android.graphics.Rect


class DetectedText(
    var text: String? = null,
    var detectedLanguages: List<String>? = null,
    var textBoxes: List<TextBox>? = null
) : ResultBase()

class TextBox(var lines: List<TextLine>? = null) : TextBase()

class TextLine(var elements: List<TextElement>? = null) : TextBase()

class TextElement : TextBase()

open class TextBase(
    var text: String? = null,
    var detectedLanguages: List<String>? = null,
    var confidence: Float? = null,
    override var boundingBox: Rect? = null,
    override var cornerPoints: List<Point>? = null
) : Positionable

