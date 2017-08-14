package com.suhang.keyboard.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.suhang.keyboard.R
import org.jetbrains.anko.dip

/**
 * Created by 苏杭 on 2017/8/14 11:28.
 */
class ShotStick : View {
    val paint = Paint()
    var strokkWidth = dip(2)
    var isStroke = true
    private var color = resources.getColor(R.color.blue)
    private var gray = resources.getColor(R.color.white)
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    override fun setBackgroundColor(color: Int) {
        this.color = color
        invalidate()
    }
    init {
        val cap = Paint.Cap.ROUND
        paint.strokeCap = cap
        paint.strokeWidth = strokkWidth.toFloat()
        paint.isAntiAlias = true
        background = ColorDrawable(resources.getColor(R.color.transparent))
    }

    override fun onDraw(canvas: Canvas) {
        val x = (width / 2).toFloat()
        val w = (width / 2).toFloat()
        if (isStroke) {
            paint.color = gray
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(x, x, w- strokkWidth, paint)
        }else{
            paint.style = Paint.Style.FILL
        }
        paint.color = color
        canvas.drawCircle(x, x, w- strokkWidth, paint)

    }
}