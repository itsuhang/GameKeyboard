package com.suhang.keyboard

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Handler
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.suhang.keyboard.constants.Constant.Companion.LOCAL_SAVE
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.event.*
import com.suhang.keyboard.utils.GsonUtil
import com.suhang.keyboard.utils.KeyHelper
import com.suhang.keyboard.utils.KeyMap
import com.suhang.keyboard.utils.SharedPrefUtil
import com.suhang.keyboard.widget.MoveButton
import com.suhang.networkmvp.function.rx.RxBusSingle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.keyboard.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


/**
 * Created by 苏杭 on 2017/8/4 15:08.
 */

class FloatKeyboard(context: Context) : AnkoLogger, MoveButton.OnContinueClickListener, View.OnTouchListener {
    var mTouchStartX = 0
    var mTouchStartY = 0
    var startX = 0
    var startY = 0
    val handler = Handler()
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.rawX.toInt()
                startY = event.rawY.toInt()
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

            MotionEvent.ACTION_UP -> {
                if (Math.abs(x - startX) < v.dip(1) || Math.abs(y - startY) < v.dip(1)) {
                    onClick(v)
                } else {
                    saveButtonToLocal()
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (Math.abs(x - startX) < v.dip(1) || Math.abs(y - startY) < v.dip(1)) {
                    onClick(v)
                } else {
                    saveButtonToLocal()
                }
            }
        }
        return false
    }


    override fun onClick(v: View) {
        if (!isEdit) {
            handler.post {
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
                    info(arrayList?.size)
                    arrayList?.forEach {
                        if (send == KeyHelper.STATUS_ON) {
                            it.button.text = s.toUpperCase()
                        } else if (send == KeyHelper.STATUS_OFF) {
                            it.button.text = s.toLowerCase()
                        } else if (send == KeyHelper.STATUS_MANAGER) {
                            if (KeyHelper.instance().isOn(KeyMap.MANAGER_ST)) {
                                isAnimating = true
                                transparent()
                                isAnimating = false
                            } else {
                                isAnimating = true
                                untransparent()
                                isAnimating = false
                            }
                        } else if (send == KeyHelper.STATUS_HOME) {
                            val intent = Intent()
                            intent.action = Intent.ACTION_MAIN
                            intent.addCategory(Intent.CATEGORY_HOME)
                            mContext.startActivity(intent)
                        } else if (send == KeyHelper.STATUS_RETURN) {
                            val intent = Intent()
                            intent.action = "com.suhang.return"
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
        } else {
            RxBusSingle.instance().post(InViewEvent(v))
        }
    }

    private var isAnimating = false

    companion object {
        var isEdit = false
        var commonWidth = 0
        var commonHeight = 0
    }

    val mContext = context
    val datas = ArrayList<ButtonData>()
    val views = HashMap<String, ArrayList<View>>()
    val mWm: WindowManager
    var viewEvent: Disposable? = null
    var saveEvent: Disposable? = null
    var deleteEvent: Disposable? = null
    var selectEvent: Disposable? = null

    init {
        commonWidth = context.resources.getDimension(R.dimen.default_width).toInt()
        commonHeight = context.resources.getDimension(R.dimen.default_height).toInt()
        onEvent()
        mWm = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        restoreButtonFromLocal()
        addKey(KeyMap.MANAGER_ST)
        viewEvent = RxBusSingle.instance().toFlowable(OutViewEvent::class.java).observeOn(AndroidSchedulers.mainThread()).subscribe({
            editKey(it.v)
            saveButtonToLocal()
        })
        saveEvent = RxBusSingle.instance().toFlowable(SaveFileEvent::class.java).observeOn(AndroidSchedulers.mainThread()).subscribe({
            saveButton(it.file)
        })
        deleteEvent = RxBusSingle.instance().toFlowable(DeleteEvent::class.java).observeOn(AndroidSchedulers.mainThread()).subscribe({
            deleteKey(it.v)
            saveButtonToLocal()
        })
        selectEvent = RxBusSingle.instance().toFlowable(SelectFileEvent::class.java).observeOn(AndroidSchedulers.mainThread()).subscribe({
            views.values.forEach {
                it.forEach {
                    mWm.removeViewImmediate(it)
                }
            }
            views.clear()
            datas.clear()
            it.list.forEach {
                restoreButton(it)
            }
        })
    }

    /**
     * 保存当前按键状态到SD卡
     */
    private fun saveButton(file: File) {
        val gson = Gson()
        val toJson = gson.toJson(datas)
        try {
            val bw = BufferedWriter(FileWriter(file))
            bw.write(toJson)
            bw.flush()
            bw.close()
            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 编辑修改按键
     */
    fun editKey(v: View) {
        val card = v as CardView
        val data = card.getTag(R.id.data) as ButtonData
        val param = card.tag as WindowManager.LayoutParams
        card.button.text = if (KeyHelper.instance().isOn(data.key)) data.key.toUpperCase() else data.key
        card.button.textSize = data.fontSize.toFloat()
        card.button.layoutParams.width = data.width
        card.button.layoutParams.height = data.height
        card.stick.layoutParams.width = data.width
        card.stick.layoutParams.height = data.height
        card.alpha = data.alpha
        card.button.requestLayout()
        val stateList = StateListDrawable()
        card.stick.setBackgroundColor(data.color)
        stateList.addState(intArrayOf(-android.R.attr.state_pressed), ColorDrawable(data.color))
        stateList.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(data.color - 0x111111))
        card.button.background = stateList
        if (data.shape == ButtonData.CIRCLE) {
            card.radius = (data.width / 2).toFloat()
        } else if (data.shape == ButtonData.SQUARE) {
            card.radius = mContext.resources.getDimension(R.dimen.default_radius)
        }
        param.x = data.x
        param.y = data.y
        param.width = data.width
        param.height = data.height
        param.alpha = data.alpha
        mWm.updateViewLayout(card, param)
    }

    /**
     * 根据数据恢复按键状态
     */
    fun restoreButton(data: ButtonData) {
        val key = data.key
        if (key == KeyMap.MANAGER_ST && views[KeyMap.MANAGER_ST] != null) {
            return
        }
        val param = getLayoutParam()
        val button = View.inflate(mContext, R.layout.keyboard, null) as CardView
        if (key == KeyMap.MANAGER_STICK) {
            data.shape = ButtonData.CIRCLE
            button.button.visibility = View.GONE
            button.stick.visibility = View.VISIBLE
            button.stick.setTag(R.id.data,data)
            button.stick.setBackgroundColor(data.color)
        }
        button.button.setOnContinueClickListener(this)
        button.button.setOnClickListener { }
        button.setOnTouchListener(this)
        button.button.text = if (KeyHelper.instance().isOn(data.key)) data.key.toUpperCase() else data.key
        button.button.textSize = data.fontSize.toFloat()
        val stateList = StateListDrawable()
        stateList.addState(intArrayOf(-android.R.attr.state_pressed), ColorDrawable(data.color))
        stateList.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(data.color - 0x111111))
        button.button.background = stateList
        if (data.shape == ButtonData.CIRCLE) {
            button.radius = (data.width / 2).toFloat()
        } else {
            button.radius = mContext.resources.getDimension(R.dimen.default_radius)
        }
        param.x = data.x
        param.y = data.y
        param.width = data.width
        param.height = data.height
        val layoutParam = FrameLayout.LayoutParams(data.width, data.height)
        button.button.layoutParams = layoutParam
        button.stick.layoutParams = layoutParam
        param.alpha = data.alpha
        button.alpha = data.alpha

        button.tag = param
        button.setTag(R.id.data, data)
        button.button.setTag(R.id.data, data)
        if (key == KeyMap.MANAGER_ST) {
            button.setTag(R.id.main, KeyMap.MANAGER_ST)
        }
        datas.add(data)
        var array = views[data.key]
        if (array == null) {
            array = ArrayList()
        }
        array.add(button)
        views.put(data.key, array)
        mWm.addView(button, param)
    }

    /**
     * 添加按键
     */
    fun addKey(key: String) {
        val data = createButtonData(key)
        data?.let {
            mWm.addView(it, it.tag as WindowManager.LayoutParams)
            saveButtonToLocal()
        }
    }

    /**
     * 删除按键
     */
    fun deleteKey(v: View) {
        val data = v.getTag(R.id.data) as ButtonData
        val main = v.getTag(R.id.main)
        if (main != null && (main as String) == KeyMap.MANAGER_ST) {
            Toast.makeText(mContext, "该按键不允许删除!", Toast.LENGTH_SHORT).show()
            return
        }
        var isBlank = false
        var key: String? = null
        views.entries.forEach {
            if (it.value.contains(v)) {
                key = it.key
                it.value.remove(v)
            }
            if (it.value.size == 0) {
                isBlank = true
            }
        }
        if (key != null && isBlank) {
            views.remove(key!!)
        }
        datas.remove(data)
        info(key)
        mWm.removeViewImmediate(v)
    }


    /**
     * 调出软键盘
     */
    private fun showKeyboard() {
        (mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 暂时隐藏键盘
     */
    fun transparent() {
        views.entries.forEach {
            if (it.key != KeyMap.MANAGER_ST) {
                it.value.forEach {
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
    }

    /**
     * 取消隐藏键盘
     */
    fun untransparent() {
        views.entries.forEach {
            if (it.key != KeyMap.MANAGER_ST) {
                it.value.forEach {
                    val view = it
                    view.tag?.let {
                        val param = it as WindowManager.LayoutParams
                        val data = view.getTag(R.id.data) as ButtonData
                        param.alpha = data.alpha
                        param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        try {
                            mWm.updateViewLayout(view, param)
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建按钮数据
     */
    fun createButtonData(key: String): View? {
        if (key == KeyMap.MANAGER_ST && views[KeyMap.MANAGER_ST] != null) {
//            Toast.makeText(mContext,"该按键为控制键,只能有一个",Toast.LENGTH_SHORT).show()
            return null
        }
        var data: ButtonData? = null
        datas.forEach {
            if (it.key == key) {
                data = it
            }
        }
        if (data != null && !FloatService.isCheck) {
            Toast.makeText(mContext, "按键重复添加!!!", Toast.LENGTH_SHORT).show()
            return null
        }
        val buttonData = ButtonData(key, commonWidth, commonHeight, 0, 0, 12, mContext.resources.getColor(R.color.gray), 1.0f, ButtonData.SQUARE, MoveButton.INTERVAL_TIME)
        val param = getLayoutParam()
        val button = View.inflate(mContext, R.layout.keyboard, null)
        if (key == KeyMap.MANAGER_STICK) {
            buttonData.width = mContext.resources.getDimension(R.dimen.default_stick_size).toInt()
            buttonData.height = mContext.resources.getDimension(R.dimen.default_stick_size).toInt()
            buttonData.shape = ButtonData.CIRCLE
            button.button.visibility = View.GONE
            button.stick.visibility = View.VISIBLE
            button.stick.setTag(R.id.data,buttonData)
            button.stick.setBackgroundColor(buttonData.color)
            param.width = buttonData.width
            param.height = buttonData.height
        }
        val stateList = StateListDrawable()
        stateList.addState(intArrayOf(-android.R.attr.state_pressed), ColorDrawable(buttonData.color))
        stateList.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(buttonData.color - 0x111111))
        button.button.background = stateList
        button.button.setOnContinueClickListener(this)
        button.button.setOnClickListener { }
        button.button.text = key
        button.setOnTouchListener(this)
        button.tag = param
        button.setTag(R.id.data, buttonData)
        button.button.setTag(R.id.data, buttonData)
        if (key == KeyMap.MANAGER_ST) {
            button.setTag(R.id.main, KeyMap.MANAGER_ST)
        }
        datas.add(buttonData)
        var array = views[key]
        if (array == null) {
            array = ArrayList()
        }
        array.add(button)
        views.put(key, array)
        return button
    }


    /**
     * 自动保存当前按键状态到本地
     */
    private fun saveButtonToLocal() {
        val gson = Gson()
        val toJson = gson.toJson(datas)
        SharedPrefUtil.putString(LOCAL_SAVE, toJson)
    }

    /**
     * 自动恢复上次按键状态
     */
    private fun restoreButtonFromLocal() {
        val json = SharedPrefUtil.getString(LOCAL_SAVE, "")
        if (!json.isBlank()) {
            val data = GsonUtil.getData(json)
            data.forEach {
                info(it)
                restoreButton(it)
            }
        }
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

    /**
     * 退出后清除悬浮窗
     */
    fun destory() {
        views.values.forEach {
            it.forEach {
                mWm.removeViewImmediate(it)
            }
        }
        views.clear()
        datas.clear()
        viewEvent?.dispose()
        saveEvent?.dispose()
        deleteEvent?.dispose()
        selectEvent?.dispose()
        KeyHelper.instance().reInit()
    }
}
