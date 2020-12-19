package com.jakebarnby.camdroid.poses.extensions

import com.google.mlkit.vision.pose.PoseLandmark
import com.jakebarnby.camdroid.models.DetectedPose

object DetectedPoseExtensions {

    fun PoseLandmark.toDetectedPose() = DetectedPose(
        position,
        landmarkFromLandmark(landmarkType),
        inFrameLikelihood
    )

    private fun landmarkFromLandmark(@PoseLandmark.LandmarkType og: Int) = when (og) {
        PoseLandmark.LEFT_ANKLE -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_ANKLE
        PoseLandmark.LEFT_EAR -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_EAR
        PoseLandmark.LEFT_ELBOW -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_ELBOW
        PoseLandmark.LEFT_EYE -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_EYE
        PoseLandmark.LEFT_EYE_INNER -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_EYE_INNER
        PoseLandmark.LEFT_EYE_OUTER -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_EYE_OUTER
        PoseLandmark.LEFT_FOOT_INDEX -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_FOOT_INDEX
        PoseLandmark.LEFT_HEEL -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_HEEL
        PoseLandmark.LEFT_HIP -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_HIP
        PoseLandmark.LEFT_INDEX -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_INDEX
        PoseLandmark.LEFT_KNEE -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_KNEE
        PoseLandmark.LEFT_MOUTH -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_MOUTH
        PoseLandmark.LEFT_PINKY -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_PINKY
        PoseLandmark.LEFT_SHOULDER -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_SHOULDER
        PoseLandmark.LEFT_THUMB -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_THUMB
        PoseLandmark.LEFT_WRIST -> com.jakebarnby.camdroid.models.PoseLandmark.LEFT_WRIST
        PoseLandmark.NOSE -> com.jakebarnby.camdroid.models.PoseLandmark.NOSE
        PoseLandmark.RIGHT_ANKLE -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_ANKLE
        PoseLandmark.RIGHT_EAR -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_EAR
        PoseLandmark.RIGHT_ELBOW -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_ELBOW
        PoseLandmark.RIGHT_EYE -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_EYE
        PoseLandmark.RIGHT_EYE_INNER -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_EYE_INNER
        PoseLandmark.RIGHT_EYE_OUTER -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_EYE_OUTER
        PoseLandmark.RIGHT_FOOT_INDEX -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_FOOT_INDEX
        PoseLandmark.RIGHT_HEEL -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_HEEL
        PoseLandmark.RIGHT_HIP -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_HIP
        PoseLandmark.RIGHT_INDEX -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_INDEX
        PoseLandmark.RIGHT_KNEE -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_KNEE
        PoseLandmark.RIGHT_MOUTH -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_MOUTH
        PoseLandmark.RIGHT_PINKY -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_PINKY
        PoseLandmark.RIGHT_SHOULDER -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_SHOULDER
        PoseLandmark.RIGHT_THUMB -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_THUMB
        PoseLandmark.RIGHT_WRIST -> com.jakebarnby.camdroid.models.PoseLandmark.RIGHT_WRIST
        else -> throw IllegalArgumentException("No mappable pose landmark type")
    }
}