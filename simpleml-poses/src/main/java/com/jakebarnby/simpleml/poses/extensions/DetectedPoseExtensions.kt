package com.jakebarnby.simpleml.poses.extensions

import com.google.mlkit.vision.pose.PoseLandmark
import com.jakebarnby.simpleml.models.pose.DetectedPose

object DetectedPoseExtensions {

    fun PoseLandmark.toDetectedPose() = DetectedPose(
        position,
        landmarkFromLandmark(landmarkType),
        inFrameLikelihood
    )

    private fun landmarkFromLandmark(@PoseLandmark.LandmarkType og: Int) = when (og) {
        PoseLandmark.LEFT_ANKLE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_ANKLE
        PoseLandmark.LEFT_EAR -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_EAR
        PoseLandmark.LEFT_ELBOW -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_ELBOW
        PoseLandmark.LEFT_EYE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_EYE
        PoseLandmark.LEFT_EYE_INNER -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_EYE_INNER
        PoseLandmark.LEFT_EYE_OUTER -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_EYE_OUTER
        PoseLandmark.LEFT_FOOT_INDEX -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_FOOT_INDEX
        PoseLandmark.LEFT_HEEL -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_HEEL
        PoseLandmark.LEFT_HIP -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_HIP
        PoseLandmark.LEFT_INDEX -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_INDEX
        PoseLandmark.LEFT_KNEE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_KNEE
        PoseLandmark.LEFT_MOUTH -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_MOUTH
        PoseLandmark.LEFT_PINKY -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_PINKY
        PoseLandmark.LEFT_SHOULDER -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_SHOULDER
        PoseLandmark.LEFT_THUMB -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_THUMB
        PoseLandmark.LEFT_WRIST -> com.jakebarnby.simpleml.models.pose.PoseLandmark.LEFT_WRIST
        PoseLandmark.NOSE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.NOSE
        PoseLandmark.RIGHT_ANKLE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_ANKLE
        PoseLandmark.RIGHT_EAR -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_EAR
        PoseLandmark.RIGHT_ELBOW -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_ELBOW
        PoseLandmark.RIGHT_EYE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_EYE
        PoseLandmark.RIGHT_EYE_INNER -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_EYE_INNER
        PoseLandmark.RIGHT_EYE_OUTER -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_EYE_OUTER
        PoseLandmark.RIGHT_FOOT_INDEX -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_FOOT_INDEX
        PoseLandmark.RIGHT_HEEL -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_HEEL
        PoseLandmark.RIGHT_HIP -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_HIP
        PoseLandmark.RIGHT_INDEX -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_INDEX
        PoseLandmark.RIGHT_KNEE -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_KNEE
        PoseLandmark.RIGHT_MOUTH -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_MOUTH
        PoseLandmark.RIGHT_PINKY -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_PINKY
        PoseLandmark.RIGHT_SHOULDER -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_SHOULDER
        PoseLandmark.RIGHT_THUMB -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_THUMB
        PoseLandmark.RIGHT_WRIST -> com.jakebarnby.simpleml.models.pose.PoseLandmark.RIGHT_WRIST
        else -> throw IllegalArgumentException("No mappable pose landmark type")
    }
}