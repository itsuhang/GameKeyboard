package com.suhang.keyboard.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.suhang.keyboard.R
import com.suhang.networkmvp.function.rx.RxBusSingle
import java.io.File
import android.view.Gravity
import android.view.WindowManager
import com.suhang.keyboard.event.SaveFileEvent
import kotlinx.android.synthetic.main.savestyle_dialog_layout.view.*


/**
 * Created by 苏杭 on 2017/8/11 10:02.
 */
class SaveStyleDialog(context: Context) : Dialog(context) {
    val view = View.inflate(context, R.layout.savestyle_dialog_layout, null)!!
    val file = File("${Environment.getExternalStorageDirectory()}/suhang")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogWindow = window
        val lp = dialogWindow.attributes
        dialogWindow.setGravity(Gravity.CENTER)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        setTitle("保存键盘")
        setCancelable(true)
        setContentView(view)
        initView()
    }

    private fun initView() {
        view.btn_cancel.setOnClickListener {
            dismiss()
        }
        view.btn_certain.setOnClickListener {
            val name = view.et_name.text.toString()
            if (name.isBlank()) {
                Toast.makeText(context, "名称不能为空", Toast.LENGTH_SHORT).show()
            } else {
                RxBusSingle.instance().post(SaveFileEvent(File(file,"$name.kb")))
                view.et_name.setText("")
                dismiss()
            }
        }
    }
}