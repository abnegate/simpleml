package com.jakebarnby.camdroid.models

data class ObjectOptions(
    val detectorMode: DetectorMode = DetectorMode.STREAM,
    val minimumConfidence: Float = 0.5f,
    val detectMultiple: Boolean = true
)