package com.suhang.keyboard.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    override fun setBackgroundColor(color: Int) {
        this.color = color
    }
    init {
        val cap = Paint.Cap.ROUND
        paint.strokeCap = cap
        paint.strokeWidth = strokkWidth.toFloat()
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        if (isStroke) {
            paint.style = Paint.Style.STROKE
        }else{
            paint.style = Paint.Style.FILL
        }
        paint.color = color
        val x = (width / 2).toFloat()
        val w = (width / 2).toFloat()
        canvas.drawCircle(x, x, w- strokkWidth, paint)
    }
}