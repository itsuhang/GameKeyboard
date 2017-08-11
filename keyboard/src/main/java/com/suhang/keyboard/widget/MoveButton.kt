package com.suhang.keyboard.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.suhang.keyboard.FloatKeyboard
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AnkoLogger
import java.util.concurrent.TimeUnit

/**
 * Created by 苏杭 on 2017/8/4 22:10.
 */

class MoveButton : TextView, AnkoLogger {
    companion object{
        val INTERVAL_TIME:Long = 50
    }
    constructor(context: Context) : super(context)
    private var intervalTask:Disposable? = null
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (FloatKeyboard.isEdit) {
            return false
        } else {
            super.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN->{
                    intervalTask = Flowable.interval(INTERVAL_TIME, INTERVAL_TIME,TimeUnit.MILLISECONDS).subscribe({
                        listener?.onClick(this)
                    })
                }
                MotionEvent.ACTION_UP -> {
                    intervalTask?.dispose()
                    listener?.onClick(this)
                }
                MotionEvent.ACTION_CANCEL -> {
                    intervalTask?.dispose()
                    listener?.onClick(this)
                }
            }
            return true
        }
    }

    interface OnContinueClickListener{
        fun onClick(v: View)
    }
    private var listener:OnContinueClickListener? = null
    fun setOnContinueClickListener(listener: OnContinueClickListener) {
        this.listener = listener
    }
}
