package com.suhang.keyboard

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import com.suhang.keyboard.utils.KeyMap
import com.suhang.keyboard.widget.SelectButtonPop
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.contentView
import org.jetbrains.anko.startService


class MainActivity : AppCompatActivity(), AnkoLogger {
    lateinit var manager: InputMethodManager
    val connect = MyConnect()
    var move: IMove? = null
    lateinit var pop:SelectButtonPop
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pop = SelectButtonPop(this)
        manager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        startService<FloatService>()
        val intent = Intent(this, FloatService::class.java)
        bindService(intent, connect, Context.BIND_AUTO_CREATE)
        initEvent()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        pop.onConfigChanged(newConfig.orientation)
    }

    inner class MyConnect : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            move = IMove.Stub.asInterface(service)
            move?.setOnShowListener(object : IShowKeyboard.Stub() {
                override fun show() {
                    showKeyboard()
                }
            })
        }
    }

    fun showKeyboard() {
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun initEvent() {
        pop.setOnDismissListener {
            move?.setVisible(true)
        }
        btn_edit.setOnClickListener {
            val m = move
            m?.let {
                m.isEdit = !m.isEdit
                btn_edit.text = if (m.isEdit) "取消编辑" else "编辑模式"
            }
        }

        button.setOnClickListener {
            move?.addKey(KeyMap.keyMap.keys.elementAt(0))
        }

        button2.setOnClickListener {
            move?.setVisible(false)
            pop.showAtLocation(contentView, Gravity.BOTTOM, 0, 0)
        }
    }

    override fun onBackPressed() {
        if (pop.isShowing) {
            pop.dismiss()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connect)
    }
}
