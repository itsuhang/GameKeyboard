package com.suhang.keyboard.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.suhang.keyboard.R
import com.suhang.keyboard.adapter.SelectStyleAdapter
import com.suhang.keyboard.event.SelectFileEvent
import com.suhang.networkmvp.function.rx.RxBusSingle
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.selectstyle_dialog_layout.view.*
import java.io.File

/**
 * Created by 苏杭 on 2017/8/11 11:41.
 */
class SelectStyleDialog(context: Context) : Dialog(context) {
    private val view = View.inflate(context, R.layout.selectstyle_dialog_layout, null)!!
    private val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    private val adapter = SelectStyleAdapter(context,SelectStyleAdapter.TYPE_DELETE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogWindow = window
        val lp = dialogWindow.attributes
        dialogWindow.setGravity(Gravity.CENTER)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        setTitle("读取键盘")
        setCancelable(true)
        setContentView(view)
        initView()
    }

    fun refresh(files: ArrayList<File>) {
        adapter.refresh(files)
    }

    private var dispose: Disposable? = null
    private fun initView() {
        view.rv.layoutManager = manager
        view.rv.adapter = adapter
        view.btn_cancel.setOnClickListener {
            dismiss()
        }
        dispose = RxBusSingle.instance().toFlowable(SelectFileEvent::class.java).subscribe({
            dismiss()
        })
    }

    override fun dismiss() {
        super.dismiss()
    }

    fun destroy() {
        dispose?.dispose()
    }
}