package com.suhang.keyboard.widget

import android.app.Activity
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import com.suhang.keyboard.R
import com.suhang.keyboard.adapter.GroupButtonAdapter
import com.suhang.keyboard.event.CombinationEvent
import com.suhang.keyboard.utils.ScreenUtils
import com.suhang.networkmvp.function.rx.RxBusSingle
import kotlinx.android.synthetic.main.select_button_pop_layout.view.*
import org.jetbrains.anko.configuration
import org.jetbrains.anko.dip

/**
 * Created by 苏杭 on 2017/8/15 11:33.
 */
class CombinationKeyPop(activity: Activity) : PopupWindow() {
    val mActivity = activity
    val root = View.inflate(activity, R.layout.select_button_pop_layout, null)!!
    val manager = GridLayoutManager(activity, 7, GridLayoutManager.VERTICAL, false)
    val adapter = GroupButtonAdapter()

    init {
        root.rv.adapter = adapter
        root.rv.layoutManager = manager
        contentView = root
        isOutsideTouchable = true
        isFocusable = true
        width = ScreenUtils.getScreenWidth(activity)
        if (activity.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.spanCount = 12
            height = root.dip(250)
        } else {
            manager.spanCount = 7
            height = root.dip(300)
        }
        root.et_name.visibility = View.VISIBLE
        root.btn_cancel.setOnClickListener {
            dismiss()
        }
        root.btn_ok.visibility = View.VISIBLE
        root.btn_ok.setOnClickListener {
            val key = adapter.getKey()
            if (key == null) {
                Toast.makeText(mActivity, "没有选择按键", Toast.LENGTH_SHORT).show()
            } else {
                if (root.et_name.text.isBlank()) {
                    Toast.makeText(mActivity, "按键名不能为空或不能只有一个", Toast.LENGTH_SHORT).show()
                } else {
                    RxBusSingle.instance().post(CombinationEvent(root.et_name.text.toString(),key))
                    root.et_name.setText("")
                }
            }
            dismiss()
        }
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        adapter.clear()
    }

    fun onConfigChanged(orientation: Int) {
        dismiss()
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.spanCount = 12
            height = root.dip(250)
            width = ScreenUtils.getScreenWidth(mActivity)
            adapter.notifyDataSetChanged()
        } else {
            manager.spanCount = 7
            height = root.dip(300)
            width = ScreenUtils.getScreenWidth(mActivity)
            adapter.notifyDataSetChanged()
        }
    }
}