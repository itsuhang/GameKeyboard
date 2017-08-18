package com.suhang.keyboard.widget

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.SeekBar
import com.suhang.keyboard.R
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.event.AllEditEvent
import com.suhang.networkmvp.function.rx.RxBusSingle
import kotlinx.android.synthetic.main.all_adjust_pop_layout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dip
import org.jetbrains.anko.px2dip

/**
 * Created by 苏杭 on 2017/8/10 14:02.
 */
class AllAdjustPop(activity: Activity) : PopupWindow(), AnkoLogger {
    val view = View.inflate(activity, R.layout.all_adjust_pop_layout, null)!!
    val pop = SelectButtonPop(activity, SelectButtonPop.STATUS_TWO)
    val data = ButtonData()
    init {
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.MATCH_PARENT
        contentView = view
        isOutsideTouchable = true
        view.button.setOnClickListener {
            RxBusSingle.instance().post(AllEditEvent(data))
            dismiss()
        }
        initData()
        initEvent()
    }

    private fun initData() {
        view.txt_button.text = data.key
        view.txt_button.layoutParams.width = data.width
        view.txt_button.layoutParams.height = data.height
        view.txt_buttonsize.text = "${view.px2dip(data.width).toInt()}"
        view.txt_button.requestLayout()
        view.sb_buttonsize.progress = view.px2dip(data.width).toInt()
        data.fontSize = data.fontSize
        view.sb_fontsize.progress = data.fontSize
        view.txt_fontsize.text = "${data.fontSize}"
        view.button.alpha = data.alpha
        view.sb_transparent.progress = (data.alpha * 100).toInt()
        view.txt_transparent.text = "${(data.alpha * 100).toInt()}"
        view.txt_button.setBackgroundColor(data.color)
        if (data.shape == ButtonData.CIRCLE) {
            view.button.radius = (data.width / 2).toFloat()
            view.cb_circle.isChecked = true
            view.cb_square.isChecked = false
        } else {
            view.button.radius = view.context.resources.getDimension(R.dimen.default_radius)
            view.cb_circle.isChecked = false
            view.cb_square.isChecked = true
        }
    }

    private fun initEvent() {
        view.sb_buttonsize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view.txt_buttonsize.text = "$progress"
                data.width = view.dip(progress)
                data.height = view.dip(progress)
                if (data.shape == ButtonData.CIRCLE) {
                    view.button.radius = (data.width / 2).toFloat()
                }
                view.txt_button.layoutParams.width = view.dip(progress)
                view.txt_button.layoutParams.height = view.dip(progress)
                view.txt_button.requestLayout()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        view.sb_fontsize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view.txt_fontsize.text = "$progress"
                data.fontSize = progress
                view.txt_button.textSize = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        view.sb_transparent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view.txt_transparent.text = "$progress%"
                data.alpha = 1.0f * progress / 100
                view.button.alpha = 1.0f * progress / 100
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        view.cb_circle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data.shape = ButtonData.CIRCLE
                view.button.radius = (data.width / 2).toFloat()
                view.cb_circle.isChecked = true
                view.cb_square.isChecked = false
            } else {
                data.shape = ButtonData.SQUARE
                view.button.radius = view.context.resources.getDimension(R.dimen.default_radius)
                view.cb_circle.isChecked = false
                view.cb_square.isChecked = true
            }
        }
        view.cb_square.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data.shape = ButtonData.SQUARE
                view.button.radius = view.context.resources.getDimension(R.dimen.default_radius)
                view.cb_circle.isChecked = false
                view.cb_square.isChecked = true
            } else {
                data.shape = ButtonData.CIRCLE
                view.button.radius = (data.width / 2).toFloat()
                view.cb_circle.isChecked = true
                view.cb_square.isChecked = false
            }
        }

        view.btn_cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        pop.dismiss()
    }
}