package com.suhang.keyboard

import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startService


class MainActivity : AppCompatActivity(), AnkoLogger {
    val connect = MyConnect()
    var move: IMove? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService<FloatService>()
        val intent = Intent(this, FloatService::class.java)
        bindService(intent, connect, Context.BIND_AUTO_CREATE)
        initEvent()
    }

    inner class MyConnect : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            move = IMove.Stub.asInterface(service)
        }

    }

    fun initEvent() {
        et.requestFocus()
        et.setOnKeyListener { v, keyCode, event ->
            info("${event.action}   $keyCode  ${event.flags}")
            false
        }
        btn_edit.setOnClickListener {
//            Thread({
//                val init = Instrumentation()
////                init.sendKeyDownUpSync(KeyEvent.KEYCODE_SHIFT_LEFT)
//                init.sendKeyDownUpSync(KeyEvent.KEYCODE_A)
//            }).start()
            val m = move
            m?.let {
                m.isEdit = !m.isEdit
                btn_edit.text = if (m.isEdit) "取消编辑" else "编辑模式"
            }
        }

        button.setOnClickListener {
            Thread({
                val init = Instrumentation()
                init.sendKeyDownUpSync(KeyEvent.KEYCODE_CAPS_LOCK)
            }).start()
        }

        button2.setOnClickListener {
            Thread({
                val init = Instrumentation()
                init.sendKeyDownUpSync(KeyEvent.KEYCODE_SHIFT_LEFT)
            }).start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connect)
    }
}