package com.suhang.keyboard

import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import android.view.WindowManager.LayoutParams.*
import android.widget.Button
import android.widget.TextView
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.utils.KeyHelper
import kotlinx.android.synthetic.main.keyboard.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


/**
 * Created by 苏杭 on 2017/8/4 15:08.
 */

class FloatKeyboard(context: Context) : AnkoLogger, View.OnClickListener, View.OnTouchListener {
    var mTouchStartX = 0
    var mTouchStartY = 0
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.tag?.let {
                    val data = it as ButtonData
                    mTouchStartX = event.rawX.toInt() - data.windowParam.x
                    mTouchStartY = event.rawY.toInt() - data.windowParam.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isEdit) {
                    v.tag?.let {
                        val data = it as ButtonData
                        info("x:${data.windowParam.x}  y:${data.windowParam.y}  rawX:${event.rawX}   rawY:${event.rawY}")
                        data.windowParam.x = (x - mTouchStartX)
                        data.windowParam.y = (y - mTouchStartY)
                        mWm.updateViewLayout(v, data.windowParam)
                    }
                }
            }
        }
        return false
    }

    //TODO 长按Shift  先Down,结束时Up
    override fun onClick(v: View) {
        val s = (v as TextView).text
        KeyHelper.instance().send(s.toString())
//        val c = (v.tag as Char)
//        val i = c.toInt()
//        if (c == 'x') {
//            KeyHelper.instance().shift()
//        } else if (c == 'y') {
//            KeyHelper.instance().capital()
//        }else if(c=='z') {
//            KeyHelper.instance().send(49)
//        }else if (c == 'v') {
//            KeyHelper.instance().ctrl()
//        } else {
//            KeyHelper.instance().send(i)
//        }

    }

    companion object {
        var isEdit = false
        var commonWidth = 0
        var commonHeight = 0
    }

    val mContext = context
    val views = ArrayList<View>()
    val mWm: WindowManager

    init {
        commonWidth = context.resources.getDimension(R.dimen.default_width).toInt()
        commonHeight = context.resources.getDimension(R.dimen.default_height).toInt()
        onEvent()
        mWm = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager


        var data = createButtonData()
        data.botton.text = "KEY"
        views + data
        mWm.addView(data, (data.tag as ButtonData).windowParam)
        data = createButtonData()
        data.botton.text = "SHL"
        views + data
        mWm.addView(data, (data.tag as ButtonData).windowParam)
        data = createButtonData()
        data.botton.text = "CAP"
        views + data
        mWm.addView(data, (data.tag as ButtonData).windowParam)
        data = createButtonData()
        data.botton.text = "1"
        views + data
        mWm.addView(data, (data.tag as ButtonData).windowParam)
        data = createButtonData()
        data.botton.text = "A"
        views + data
        mWm.addView(data, (data.tag as ButtonData).windowParam)
    }

    /**
     * 创建按钮数据
     */
    fun createButtonData(): View {
        val buttonData = ButtonData(commonWidth, commonHeight, getLayoutParam())
        val button = View.inflate(mContext, R.layout.keyboard, null)
        button.botton.setOnClickListener(this)
        button.setOnTouchListener(this)
        button.tag = buttonData
        return button
    }


    /**
     * 创建悬浮窗参数
     */
    fun getLayoutParam(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        params.type = WindowManager.LayoutParams.TYPE_TOAST
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        params.flags = FLAG_NOT_TOUCH_MODAL or FLAG_NOT_FOCUSABLE
        // 设置图片格式，效果为背景透明
        params.format = PixelFormat.TRANSLUCENT
        params.gravity = Gravity.TOP
        params.x = 0
        params.y = 0
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        return params
    }

    private fun onEvent() {
    }

    fun destory() {
    }
}
