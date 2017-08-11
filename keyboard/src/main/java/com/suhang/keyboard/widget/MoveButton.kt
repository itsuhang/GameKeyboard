package com.suhang.keyboard.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.suhang.keyboard.FloatKeyboard
import com.suhang.keyboard.R
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.utils.KeyMap
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

/**
 * Created by 苏杭 on 2017/8/4 22:10.
 */

class MoveButton : TextView, AnkoLogger {
    companion object {
        val INTERVAL_TIME: Long = 50
    }

    constructor(context: Context) : super(context)

    private var intervalTask: Disposable? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    lateinit var data: ButtonData

    override fun setTag(key: Int, tag: Any?) {
        super.setTag(key, tag)
        if (key == R.id.data) {
            info(tag)
            data = tag as ButtonData
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (FloatKeyboard.isEdit) {
            return false
        } else {
            super.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!KeyMap.isSpecalKey(data.key)) {
                        intervalTask = Flowable.interval(INTERVAL_TIME, INTERVAL_TIME, TimeUnit.MILLISECONDS).subscribe({
                            listener?.onClick(this@MoveButton)
                        })
                    }
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

    interface OnContinueClickListener {
        fun onClick(v: View)
    }

    private var listener: OnContinueClickListener? = null
    fun setOnContinueClickListener(listener: OnContinueClickListener) {
        this.listener = listener
    }
}
