package com.suhang.keyboard.widget

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.suhang.keyboard.R
import com.suhang.keyboard.utils.ScreenUtils
import kotlinx.android.synthetic.main.color_picker_pop_layout.view.*
import kotlinx.android.synthetic.main.keyboard.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by 苏杭 on 2017/8/10 14:02.
 */
class ColorPickerPop(activity: Activity) : PopupWindow(),AnkoLogger {
    val mActivity:Activity = activity
    val view = View.inflate(activity, R.layout.color_picker_pop_layout, null)!!
    init {
        view.colorPicker.addSVBar(view.svbar)
        view.colorPicker.addOpacityBar(view.opacitybar)
        width = ScreenUtils.getScreenWidth(activity)
        height = WindowManager.LayoutParams.WRAP_CONTENT
        contentView = view
        isOutsideTouchable = true
        view.button.setOnClickListener {
            view.txt_button.setBackgroundColor(view.colorPicker.color-0x111111)
        }
    }
}