package com.jakebarnby.simpleml.classification

import android.graphics.Rect

/**
 * An image classification result with classified label and percent confidence
 *
 *
 * Created by jbarnby 14/5/2018.
 */
data class ClassifiedResult(
    /**
     * Object that was classified
     */
    val label: String,
    /**
     * Estimated percent accuracy of the classification
     */
    val confidence: Float,
    /**
     * Bounding box of the classified object
     */
    var boundingBox: Rect? = null
)
