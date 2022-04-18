package com.jakebarnby.simpleml.poses.view

import android.content.Context
import android.util.AttributeSet
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.jakebarnby.simpleml.camera2.view.Camera2View
import com.jakebarnby.simpleml.models.pose.DetectedPose
import com.jakebarnby.simpleml.models.pose.PoseOptions
import com.jakebarnby.simpleml.poses.analyzer.LocalPoseAnalyzer
import com.jakebarnby.simpleml.poses.extensions.DetectedPoseExtensions.toDetectedPose
import com.jakebarnby.simpleml.poses.extensions.PoseOptionsExtensions.toPoseDetectorOptions

class LocalPoseAnalyzerView : Camera2View<PoseDetector,
        PoseDetectorOptionsBase,
        ImageProxy,
        List<PoseLandmark>,
        List<DetectedPose>> {

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    override fun init(attrs: AttributeSet) {
        super.init(attrs)

        setAnalyzer<LocalPoseAnalyzer>(
            (options as PoseOptions).toPoseDetectorOptions()
        )
    }

    override fun setOnNextDetectionListener(onNext: (List<DetectedPose>) -> Unit) {
        presenter?.analyzer?.onAnalysisResult = { results ->
            onNext(results.map { it.toDetectedPose() })
        }
    }
}