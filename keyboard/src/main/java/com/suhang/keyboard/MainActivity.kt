package com.suhang.keyboard

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.suhang.keyboard.constants.Constant
import com.suhang.keyboard.event.ClickEvent
import com.suhang.keyboard.event.InViewEvent
import com.suhang.keyboard.utils.KeyHelper
import com.suhang.keyboard.utils.SharedPrefUtil
import com.suhang.keyboard.widget.*
import com.suhang.networkmvp.function.rx.RxBusSingle
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.contentView
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast
import java.io.File


class MainActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var manager: InputMethodManager
    private val connect = MyConnect()
    private var move: IMove? = null
    private lateinit var pop: SelectButtonPop
    private var colorPop: ColorPickerPop? = null
    private lateinit var combinationPop: CombinationKeyPop
    private lateinit var saveDialog: SaveStyleDialog
    private lateinit var loadDialog: SelectStyleDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBox.isChecked = SharedPrefUtil.getBoolean(Constant.CHECK_STATUS, false)
        pop = SelectButtonPop(this, SelectButtonPop.STATUS_ONE)
        combinationPop = CombinationKeyPop(this)
        saveDialog = SaveStyleDialog(this)
        loadDialog = SelectStyleDialog(this)
        manager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        startService<FloatService>()
        val intent = Intent(this, FloatService::class.java)
        bindService(intent, connect, Context.BIND_AUTO_CREATE)
        initEvent()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        pop.onConfigChanged(newConfig.orientation)
        combinationPop.onConfigChanged(newConfig.orientation)
    }

    inner class MyConnect : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            move = IMove.Stub.asInterface(service)
            move?.check(checkBox.isChecked)
        }
    }

    var viewEvent: Disposable? = null
    var clickEvent: Disposable? = null
    fun initEvent() {
        viewEvent = RxBusSingle.instance().toFlowable(InViewEvent::class.java).subscribe({
            colorPop = ColorPickerPop(this, it.view)
            move?.setVisible(false)
            colorPop?.showAtLocation(contentView, Gravity.TOP, 0, 0)
            colorPop?.setOnDismissListener {
                move?.setVisible(true)
            }
        })
        clickEvent = RxBusSingle.instance().toFlowable(ClickEvent::class.java).subscribe({
            move?.addKey(it.text)
            pop.dismiss()
        })
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

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefUtil.putBoolean(Constant.CHECK_STATUS, isChecked)
            move?.check(isChecked)
        }

        btn_add.setOnClickListener {
            move?.setVisible(false)
            pop.showAtLocation(contentView, Gravity.BOTTOM, 0, 0)
        }

        btn_close.setOnClickListener {
            val intent = Intent(this, FloatService::class.java)
            stopService(intent)
            move?.destroy()
            finish()
        }
        btn_save.setOnClickListener {
            RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe({
                if (it) {
                    val dir = File(Environment.getExternalStorageDirectory(), "suhang")
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    val list = ArrayList<File>()
                    dir.listFiles().filterTo(list) { it.name.endsWith(".kb") }
                    move?.setVisible(false)
                    saveDialog.refresh(list)
                    saveDialog.show()
                } else {
                    toast("你拒绝了读写SD卡权限,无法为你保存配置")
                }
            })
        }

        combinationPop.setOnDismissListener {
            move?.setVisible(true)
        }

        btn_combination.setOnClickListener {
            move?.setVisible(false)
            combinationPop.showAtLocation(contentView,Gravity.BOTTOM,0,0)
        }
        saveDialog.setOnDismissListener {
            move?.setVisible(true)
        }
        loadDialog.setOnDismissListener {
            move?.setVisible(true)
        }

        btn_load.setOnClickListener {
            RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe({
                if (it) {
                    val dir = File(Environment.getExternalStorageDirectory(), "suhang")
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    val list = ArrayList<File>()
                    dir.listFiles().filterTo(list) { it.name.endsWith(".kb") }
                    move?.setVisible(false)
                    loadDialog.refresh(list)
                    loadDialog.show()
                } else {
                    toast("你拒绝了读写SD卡权限,无法为你保存配置")
                }
            })
        }
    }

    override fun onBackPressed() {
        val color: ColorPickerPop? = colorPop
        if (pop.isShowing||combinationPop.isShowing) {
            pop.dismiss()
            combinationPop.dismiss()
        } else if (color != null && color.isShowing) {
            color.dismiss()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        move?.isEdit = false
        btn_edit.text = "编辑模式"
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connect)
        pop.dismiss()
        val color: ColorPickerPop? = colorPop
        if (color != null && color.isShowing) {
            color.dismiss()
        }
        viewEvent?.dispose()
        clickEvent?.dispose()
        loadDialog.destroy()
    }
}
