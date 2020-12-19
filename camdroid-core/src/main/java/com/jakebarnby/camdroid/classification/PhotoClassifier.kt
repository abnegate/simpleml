package com.jakebarnby.camdroid.classification

import android.graphics.Bitmap

/**
 * Classifies a collection of images and returns the results.
 *
 *
 * Created by jbarnby 14/5/2018.
 */
interface PhotoClassifier  {

    suspend fun initialise()

    /**
     * Get a set of [ClassificationResult] by classifying the given collection of [Bitmap].
     *
     * @param album                      The album for which to get labels for each photo from.
     * @param onNextClassificationResult Result listener to post results to on completion.
     */
    suspend fun classify(
        album: Collection<Bitmap>,
        onNextClassificationResult: (Collection<ClassificationResult>) -> Unit
    )

    /**
     * Get a set of [ClassificationResult] by classifying the given collection of [Bitmap].
     *
     * @param bitmap                 The image for which to get labels for.
     */
    suspend fun classify(bitmap: Bitmap): Collection<ClassificationResult>

    /**
     * Release managed resources.
     */
    fun close()
}
