package com.jakebarnby.camdroid.camera.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jakebarnby.camdroid.R
import com.jakebarnby.camdroid.extensions.getScreenHeight
import com.jakebarnby.camdroid.extensions.getScreenWidth
import kotlin.math.roundToInt


open class CameraOverlay : LinearLayout {

    private var darkAlpha = 0.7f
    private var borderWidth = 2

    private var windowFrame: Bitmap? = null
    private val areaAlpha: Int = (255 * darkAlpha).roundToInt()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setAttrs(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttrs(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setAttrs(attrs)
    }

    private fun setAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CameraOverlay,
            0, 0).apply {
            try {
                darkAlpha = getFloat(R.styleable.CameraOverlay_overlay_alpha, darkAlpha)
                borderWidth = getInteger(R.styleable.CameraOverlay_overlay_borderWidth, borderWidth)
            } finally {
                recycle()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (windowFrame == null) {
            createWindowFrame(0f, 0f, context.getScreenWidth(), context.getScreenHeight())
        }
        canvas.drawBitmap(windowFrame!!, 0f, 0f, null)
    }

    override fun isEnabled() = false

    override fun isClickable() = false

    override fun isInEditMode() = true

    private fun createWindowFrame(left: Float, top: Float, width: Float, height: Float) {
        windowFrame = Bitmap.createBitmap(
            width.toInt(),
            height.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val osCanvas = Canvas(windowFrame!!)
        val outerRectangle = RectF(
            0f,
            0f,
            this.width.toFloat(),
            this.height.toFloat())

        var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(areaAlpha, 0, 0, 0)
        }

        osCanvas.drawRect(outerRectangle, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        val innerRectangle = RectF(
            left,
            top,
            left + width,
            top + height
        )

        osCanvas.drawRect(innerRectangle, paint)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = borderWidth.toFloat()
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE

        osCanvas.drawRect(innerRectangle, paint)
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        windowFrame = null
    }

    fun moveFrameToRect(targetRect: Rect){
        createWindowFrame(
            targetRect.left.toFloat(),
            targetRect.top.toFloat(),
            targetRect.width().toFloat(),
            targetRect.height().toFloat())
        invalidate()
        requestLayout()
    }
}