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
import com.suhang.keyboard.constants.Constant
import com.suhang.keyboard.event.ClickEvent
import com.suhang.keyboard.utils.SharedPrefUtil
import com.suhang.keyboard.widget.ColorPickerPop
import com.suhang.keyboard.widget.SelectButtonPop
import com.suhang.networkmvp.function.rx.RxBusSingle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.contentView
import org.jetbrains.anko.startService


class MainActivity : AppCompatActivity(), AnkoLogger {
    lateinit var manager: InputMethodManager
    val connect = MyConnect()
    var move: IMove? = null
    lateinit var pop:SelectButtonPop
    lateinit var colorPop:ColorPickerPop
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBox.isChecked = SharedPrefUtil.getBoolean(Constant.CHECK_STATUS,false)
        pop = SelectButtonPop(this)
        colorPop = ColorPickerPop(this)
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
            move?.check(checkBox.isChecked)
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
        RxBusSingle.instance().toFlowable(ClickEvent::class.java).subscribe({
            move?.addKey(it.text)
            pop.dismiss()
        })
        pop.setOnDismissListener {
            move?.setVisible(true)
        }
        colorPop.setOnDismissListener {
            move?.setVisible(true)
        }
        btn_edit.setOnClickListener {
            val m = move
            m?.let {
                m.isEdit = !m.isEdit
                btn_edit.text = if (m.isEdit) "取消编辑" else "编辑模式"
            }
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefUtil.putBoolean(Constant.CHECK_STATUS,isChecked)
            move?.check(isChecked)
        }

        btn_test.setOnClickListener {
            move?.setVisible(false)
            colorPop.showAtLocation(contentView,Gravity.BOTTOM,0,0)
        }

        btn_add.setOnClickListener {
            move?.setVisible(false)
            pop.showAtLocation(contentView, Gravity.BOTTOM, 0, 0)
        }

        btn_close.setOnClickListener {
            val intent = Intent(this, FloatService::class.java)
            stopService(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (pop.isShowing or colorPop.isShowing) {
            pop.dismiss()
            colorPop.dismiss()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connect)
        pop.dismiss()
        colorPop.dismiss()
    }
}
