package com.suhang.keyboard

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.properties.Delegates

class FloatService : Service() {
    var isEdit by Delegates.observable(false, {
        _, _, new ->
        FloatKeyboard.isEdit = new
    })

    override fun onCreate() {
        super.onCreate()
        FloatKeyboard(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    val binder = object : IMove.Stub() {
        override fun isEdit(): Boolean {
            return this@FloatService.isEdit
        }

        override fun setEdit(isEdit: Boolean) {
            this@FloatService.isEdit = isEdit
        }
    }
}
