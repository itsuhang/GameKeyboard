package com.suhang.keyboard.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import com.suhang.keyboard.FloatKeyboard
import org.jetbrains.anko.AnkoLogger

/**
 * Created by 苏杭 on 2017/8/4 22:10.
 */

class MoveButton : TextView, AnkoLogger {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (FloatKeyboard.isEdit) {
            return false
        }
        return super.onTouchEvent(event)
    }
}
