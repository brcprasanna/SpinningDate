package com.prasanna.spinningdate.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.prasanna.spinningdate.R

class CustomSpinningTextView : TextView {
    private var mStartX = 0f
    private var mStartY = 0f

    private var mSpinAngle = 0f

    private var mXCordDiff = 0f
    private var mYCordDiff = 0f

    private val mPaint = Paint().apply { this.color = Color.parseColor(context.getString(R.string.light_blue)) }
    private val mBorderPaint = Paint().apply {
        this.color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }
    private val rectF by lazy { RectF(0f, 0f, width.toFloat(), height.toFloat()) }

    lateinit var onDrag: (event: MotionEvent?, expandGrayArea: Boolean) -> Unit

    var grayAreaTotalHeight: Float = 0f
    var grayAreaHeight: Float = 0f

    var deviceHeight: Float = 0f
    var deviceWidth: Float = 0f


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setSpinningAnimation()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setSpinningAnimation()
    }

    constructor(context: Context) : super(context) {
        setSpinningAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(mSpinAngle, 0.5f * width, 0.5f * height)
        canvas?.drawRect(rectF, mBorderPaint)
        canvas?.drawRect(rectF, mPaint)
        super.onDraw(canvas)
        canvas?.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && h != 0) {
            mStartX = x
            mStartY = y
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mXCordDiff = x - event.rawX
                mYCordDiff = y - event.rawY
                bringToFront()
                onDrag(event, false)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                x = event.rawX + mXCordDiff
                y = event.rawY + mYCordDiff
                invalidate()
                onDrag(event, false)
                true
            }
            MotionEvent.ACTION_UP -> {
                if ((y + height * 0.75f) > (deviceHeight - grayAreaHeight) && grayAreaHeight > height) {
                    animate().x(deviceWidth / 2 - width / 2)
                        .y(deviceHeight - grayAreaTotalHeight / 2 - height / 2)
                        .setDuration(AppConstants.TOUCH_EVENT_ANIM_DURATION.toLong())
                        .start()
                    onDrag(event, true)
                } else {
                    animate().x(mStartX)
                        .y(mStartY)
                        .setDuration(AppConstants.TOUCH_EVENT_ANIM_DURATION.toLong())
                        .start()
                    onDrag(event, false)
                }
                true
            }
            else -> false
        }
    }

    private fun setSpinningAnimation() {
        val spinAnimator = ValueAnimator.ofFloat(0f, 360f)
        spinAnimator.repeatCount = ValueAnimator.INFINITE
        spinAnimator.interpolator = LinearInterpolator()
        spinAnimator.addUpdateListener { animation ->
            mSpinAngle = animation.animatedValue as Float
            invalidate()
        }
        spinAnimator.duration = AppConstants.SPIN_DURATION.toLong()
        spinAnimator.start()
    }

    fun onDrag(onDrag: (event: MotionEvent?, expandGrayArea: Boolean) -> Unit) {
        this.onDrag = onDrag
    }

}