package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private var text = resources.getString(R.string.button_download)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                updateState(ButtonState.Loading)
                invalidate()
            }
            ButtonState.Loading -> {
                text = resources.getString(R.string.button_loading)
                invalidate()
            }
            ButtonState.Completed -> {
                text = resources.getString(R.string.button_loading)
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
        typeface = Typeface.MONOSPACE
        textSize = 40f
        color = getColor(context,R.color.colorPrimary)
        textAlign = Paint.Align.CENTER
    }
    private val textPaintStyle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.MONOSPACE
        textSize = 40f
        color = getColor(context,R.color.white)
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            backgroundPaintStyle.color =  getColor(context,R.color.colorPrimary)
            canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), backgroundPaintStyle)
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

}