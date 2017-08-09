package com.suhang.keyboard

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
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
                    val param = it as WindowManager.LayoutParams
                    mTouchStartX = event.rawX.toInt() - param.x
                    mTouchStartY = event.rawY.toInt() - param.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isEdit) {
                    v.tag?.let {
                        val param = it as WindowManager.LayoutParams
                        info("x:${param.x}  y:${param.y}  rawX:${event.rawX}   rawY:${event.rawY}")
                        param.x = (x - mTouchStartX)
                        param.y = (y - mTouchStartY)
                        mWm.updateViewLayout(v, param)
                        v.getTag(R.id.data)?.let {
                            val data = it as ButtonData
                            data.x = param.x
                            data.y = param.y
                        }
                    }
                }
            }
        }
        return false
    }

    override fun onClick(v: View) {
        val view = (v as TextView)
        var s = view.text.toString()
        if ("KEY" == s) {
            showKeyboard()
        } else {
            val send = KeyHelper.instance().send(s)
            var arrayList = views[s]
            if (arrayList == null) {
                s = s.toLowerCase()
            }
            arrayList = views[s]
            arrayList?.forEach {
                if (send == KeyHelper.STATUS_ON) {
                    it.botton.text = s.toUpperCase()
                } else if (send == KeyHelper.STATUS_OFF) {
                    it.botton.text = s.toLowerCase()
                }
            }

        }
    }

    companion object {
        var isEdit = false
        var commonWidth = 0
        var commonHeight = 0
    }

    val mContext = context
    val datas = HashMap<String, ButtonData>()
    val views = HashMap<String, ArrayList<View>>()
    val mWm: WindowManager

    init {
        commonWidth = context.resources.getDimension(R.dimen.default_width).toInt()
        commonHeight = context.resources.getDimension(R.dimen.default_height).toInt()
        onEvent()
        mWm = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun addKey(key: String) {
        val data = createButtonData(key)
        data?.let {
            mWm.addView(data, data.tag as WindowManager.LayoutParams)
        }
    }

    private fun showKeyboard() {
        (mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun transparent() {
        views.values.forEach {
            it.forEach {
                val view = it
                view.tag?.let {
                    val param = it as WindowManager.LayoutParams
                    param.alpha = 0f
                    param.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    mWm.updateViewLayout(view, param)
                }
            }
        }
    }

    fun untransparent() {
        views.values.forEach {
            it.forEach {
                val view = it
                view.tag?.let {
                    val param = it as WindowManager.LayoutParams
                    param.alpha = 1.0f
                    param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    mWm.updateViewLayout(view, param)
                }
            }
        }
    }

    /**
     * 创建按钮数据
     */
    fun createButtonData(key: String): View? {
        if (datas[key] != null && !FloatService.isCheck) {
            Toast.makeText(mContext, "按键重复添加!!!", Toast.LENGTH_SHORT).show()
            return null
        }
        val buttonData = ButtonData(key, commonWidth, commonHeight, 0, 0)
        val button = View.inflate(mContext, R.layout.keyboard, null)
        info(button.width)
        button.botton.setOnClickListener(this)
        button.botton.text = key
        button.setOnTouchListener(this)
        button.tag = getLayoutParam()
        button.setTag(R.id.data, buttonData)
        datas.put(key, buttonData)
        var array = views[key]
        if (array == null) {
            array = ArrayList()
        }
        array.add(button)
        views.put(key, array)
        return button
    }


    /**
     * 创建悬浮窗参数
     */
    fun getLayoutParam(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        params.flags = FLAG_NOT_FOCUSABLE
        // 设置图片格式，效果为背景透明
        params.format = PixelFormat.TRANSLUCENT
        params.gravity = Gravity.TOP
        params.x = 0
        params.y = 0
        params.width = commonWidth
        params.height = commonHeight
        return params
    }

    private fun onEvent() {
    }

    fun destory() {
    }
}
