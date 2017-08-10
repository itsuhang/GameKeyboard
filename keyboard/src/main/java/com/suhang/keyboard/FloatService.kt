package com.suhang.keyboard

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import kotlin.properties.Delegates

class FloatService : Service() {
    companion object{
        var isCheck = false
    }
    var isEdit by Delegates.observable(false, {
        _, _, new ->
        FloatKeyboard.isEdit = new
    })
    lateinit var mWm:WindowManager
    var listener:IShowKeyboard? = null
    lateinit var keyboard:FloatKeyboard
    override fun onCreate() {
        super.onCreate()
        keyboard = FloatKeyboard(this)
        mWm = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    val binder = object : IMove.Stub() {
        override fun check(isChecked: Boolean) {
            isCheck = isChecked
        }

        override fun setVisible(isVisible: Boolean) {
            if (!isVisible) {
                keyboard.transparent()
            } else {
                keyboard.untransparent()
            }
        }

        override fun addKey(key: String) {
            keyboard.addKey(key)
        }

        override fun setOnShowListener(listener: IShowKeyboard?) {
            this@FloatService.listener = listener
        }

        override fun isEdit(): Boolean {
            return this@FloatService.isEdit
        }

        override fun setEdit(isEdit: Boolean) {
            this@FloatService.isEdit = isEdit
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboard.destory()
    }
}
