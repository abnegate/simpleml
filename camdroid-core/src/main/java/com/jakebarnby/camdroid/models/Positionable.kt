package com.jakebarnby.camdroid.models

import android.graphics.Point
import android.graphics.Rect

interface Positionable {
    var boundingBox: Rect?
    var cornerPoints: List<Point>?
}