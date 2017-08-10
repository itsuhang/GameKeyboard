package com.suhang.keyboard.widget

import android.app.Activity
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.PopupWindow
import com.suhang.keyboard.R
import com.suhang.keyboard.adapter.SelectButtonAdapter
import com.suhang.keyboard.utils.ScreenUtils
import kotlinx.android.synthetic.main.select_button_pop_layout.view.*
import org.jetbrains.anko.configuration
import org.jetbrains.anko.dip

/**
 * Created by 苏杭 on 2017/8/9 20:33.
 */
class SelectButtonPop(activity: Activity,type:Int) : PopupWindow() {
    val mActivity = activity
    val root = View.inflate(activity, R.layout.select_button_pop_layout, null)
    val manager = GridLayoutManager(activity, 7, GridLayoutManager.VERTICAL, false)
    val adapter = SelectButtonAdapter(type)
    companion object{
        const val STATUS_ONE = 1
        const val STATUS_TWO = 2
    }
    init {
        root.rv.adapter = adapter
        root.rv.layoutManager = manager
        contentView = root
        isOutsideTouchable = true
        width = ScreenUtils.getScreenWidth(activity)
        if (activity.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.spanCount = 12
            height = root.dip(200)
        } else {
            manager.spanCount = 7
            height = root.dip(300)
        }
        root.btn_cancel.setOnClickListener {
            dismiss()
        }
    }


    fun onConfigChanged(orientation: Int) {
        dismiss()
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.spanCount = 12
            height = root.dip(200)
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