package com.suhang.keyboard.widget

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.suhang.keyboard.FloatKeyboard
import com.suhang.keyboard.R
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.event.SpecialKeyEvent
import com.suhang.keyboard.event.TransparentEvent
import com.suhang.keyboard.utils.KeyHelper
import com.suhang.keyboard.utils.KeyMap
import com.suhang.networkmvp.function.rx.RxBusSingle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by 苏杭 on 2017/8/4 22:10.
 */

class MoveButton : TextView, AnkoLogger {
    companion object {
        val INTERVAL_TIME: Long = 200
    }

    private var dispose: Disposable? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    lateinit var data: ButtonData

    override fun setTag(key: Int, tag: Any?) {
        super.setTag(key, tag)
        if (key == R.id.data) {
            info(tag)
            data = tag as ButtonData
            if (KeyMap.isSpecalKey(data.key)) {
                dispose = RxBusSingle.instance().toFlowable(SpecialKeyEvent::class.java).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    val lower = text.toString().toLowerCase()
                    val lower2 = it.key.toLowerCase()
                    if (lower == lower2) {
                        if (it.isOn) {
                            this@MoveButton.text = it.key.toUpperCase()
                        } else {
                            this@MoveButton.text = it.key.toLowerCase()
                        }
                    }
                })
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (FloatKeyboard.isEdit) {
            return false
        } else {
            super.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    down()
                }
                MotionEvent.ACTION_UP -> {
                    up()
                }
                MotionEvent.ACTION_CANCEL -> {
                    up()
                }
            }
            return true
        }
    }


    /**
     * 调出软键盘
     */
    private fun showKeyboard() {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun down() {
        val s = text.toString()
        if ("KEY" == s) {
            showKeyboard()
        } else {
            val send = KeyHelper.instance().sendDown(s)
            if (send == KeyHelper.STATUS_ON) {
                RxBusSingle.instance().post(SpecialKeyEvent(s,true))
            } else if (send == KeyHelper.STATUS_OFF) {
                RxBusSingle.instance().post(SpecialKeyEvent(s,false))
            } else if (send == KeyHelper.STATUS_MANAGER) {
                if (KeyHelper.instance().isOn(KeyMap.MANAGER_ST)) {
                    RxBusSingle.instance().post(TransparentEvent(true))
                } else {
                    RxBusSingle.instance().post(TransparentEvent(false))
                }
            } else if (send == KeyHelper.STATUS_HOME) {
                val intent = Intent()
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_HOME)
                context.startActivity(intent)
            } else if (send == KeyHelper.STATUS_RETURN) {
                try {
                    val intent = Intent()
                    intent.action = "com.suhang.return"
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context,"没有找到与本软件匹配的的Exagear程序!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun up() {
        val s = text.toString()
        if ("KEY" != s) {
            KeyHelper.instance().sendUp(s)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dispose?.dispose()
    }

    interface OnContinueClickListener {
        fun onClick(v: View)
    }

    private var listener: OnContinueClickListener? = null
    fun setOnContinueClickListener(listener: OnContinueClickListener) {
        this.listener = listener
    }
}
