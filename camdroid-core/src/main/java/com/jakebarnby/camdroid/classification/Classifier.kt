package com.jakebarnby.camdroid.classification

import android.graphics.Bitmap

/**
 * Classifies a collection of images and returns the results.
 */
interface Classifier {

    suspend fun initialise()

    /**
     * Get a set of [ClassifiedResult] by classifying the given collection of [Bitmap].
     *
     * @param album                      The album for which to get labels for each photo from.
     * @param onNextClassificationResult Result listener to post results to on completion.
     */
    suspend fun classify(
        album: Collection<Bitmap>,
        onNextClassificationResult: (Collection<ClassifiedResult>) -> Unit
    )

    /**
     * Get a set of [ClassifiedResult] by classifying the given collection of [Bitmap].
     *
     * @param bitmap                 The image for which to get labels for.
     */
    suspend fun classify(bitmap: Bitmap): Collection<ClassifiedResult>

    /**
     * Release managed resources.
     */
    fun close()
}
