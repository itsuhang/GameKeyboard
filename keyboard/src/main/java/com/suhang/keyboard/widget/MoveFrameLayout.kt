package com.suhang.keyboard.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.suhang.keyboard.FloatKeyboard
import com.suhang.keyboard.event.ButtonTouch
import com.suhang.networkmvp.function.rx.RxBusSingle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AnkoLogger

/**
 * Created by 苏杭 on 2017/8/4 22:21.
 */
class MoveFrameLayout : FrameLayout, AnkoLogger {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    var touchChild: View? = null
    val disable:Disposable
    init {
        disable = RxBusSingle.instance().toFlowable(ButtonTouch::class.java).observeOn(AndroidSchedulers.mainThread()).subscribe({
            touchChild = it.view
        })

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disable.dispose()
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_MOVE -> {
                return FloatKeyboard.isEdit
            }
            MotionEvent.ACTION_UP -> {
                return FloatKeyboard.isEdit
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    var startX: Int = 0
    var startY: Int = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x.toInt()
                startY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                touchChild?.x = event.x
                touchChild?.y = event.y
            }
            MotionEvent.ACTION_UP -> {
                touchChild = null
                startX = 0
                startY = 0
            }
        }
        return true
    }
}