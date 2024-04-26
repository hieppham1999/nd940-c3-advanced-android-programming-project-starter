package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat.getColor
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()
    private var text = resources.getString(R.string.button_download)

    private var progressBarWidth = 0F
    private var arcStartAngle = 0f
    private var arcSweepAngle = 0f

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                updateState(ButtonState.Loading)
                invalidate()
            }
            ButtonState.Loading -> {
                text = resources.getString(R.string.button_loading)
                animateProgressBar()
            }
            ButtonState.Completed -> {
                text = resources.getString(R.string.button_download)
                invalidate()
            }
        }
    }


    init {
    }

    fun updateState(state: ButtonState) {
        buttonState  = state
    }

    private val backgroundPaintStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = getColor(context,R.color.colorPrimary)
    }

    private val progressBarPaintStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = getColor(context,R.color.colorPrimaryDark)
    }

    private val textPaintStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 40f
        color = getColor(context,R.color.white)
        textAlign = Paint.Align.CENTER
    }

    private val circlePaintStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = getColor(context,R.color.colorAccent)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            backgroundPaintStyle.color =  getColor(context,R.color.colorPrimary)
            // draw the background
            it.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), backgroundPaintStyle)

            // draw the progress bar like rect on the background
            it.drawRect(0f, 0f, progressBarWidth, heightSize.toFloat(), progressBarPaintStyle)

            // draw the circle
            it.drawArc(
                (widthSize * 0.75).toFloat(),
                heightSize / 2 - 20f,
                widthSize * 0.75F + 40f,
                heightSize / 2 + 20f,
                arcStartAngle,
                arcSweepAngle,
                true,
                circlePaintStyle
            )
            // draw the text
            it.drawText(text,  widthSize / 2.0f, heightSize / 2.0f + 15.0f, textPaintStyle)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun animateProgressBar() {
        valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
            duration = 1500
            interpolator = AccelerateInterpolator()
            addUpdateListener { animator ->
                progressBarWidth = animator.animatedValue as Float
                arcSweepAngle =  360f / widthSize  * animator.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    isEnabled = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isEnabled = true
                    updateState(ButtonState.Completed)
                    progressBarWidth = 0F
                    arcSweepAngle = arcStartAngle

                }
            }
            )
            start()
        }
    }

}