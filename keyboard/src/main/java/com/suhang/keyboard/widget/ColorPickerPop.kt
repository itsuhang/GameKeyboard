package com.suhang.keyboard.widget

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.SeekBar
import com.suhang.keyboard.R
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.event.OutViewEvent
import com.suhang.keyboard.event.PClickEvent
import com.suhang.networkmvp.function.rx.RxBusSingle
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.color_picker_pop_layout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dip
import org.jetbrains.anko.px2dip

/**
 * Created by 苏杭 on 2017/8/10 14:02.
 */
class ColorPickerPop(activity: Activity, v: View) : PopupWindow(), AnkoLogger {
    val mActivity = activity
    val view = View.inflate(activity, R.layout.color_picker_pop_layout, null)!!
    val data: ButtonData = v.getTag(R.id.data) as ButtonData
    val pop = SelectButtonPop(activity, SelectButtonPop.STATUS_TWO)

    init {
        view.colorPicker.addSVBar(view.svbar)
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.MATCH_PARENT
        contentView = view
        isOutsideTouchable = true
        view.button.setOnClickListener {
            data.color = view.colorPicker.color
            RxBusSingle.instance().post(OutViewEvent(v))
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
        view.colorPicker.color = data.color
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

    var clickEvent: Disposable? = null
    private fun initEvent() {
        clickEvent = RxBusSingle.instance().toFlowable(PClickEvent::class.java).subscribe({
            view.txt_button.text = it.text
            data.key = it.text
            pop.dismiss()
        })
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
        view.colorPicker.setOnColorChangedListener {
            view.txt_button.setBackgroundColor(it)
        }
        view.btn_cancel.setOnClickListener {
            dismiss()
        }
        view.btn_changekey.setOnClickListener {
            pop.showAtLocation(mActivity.window.decorView, Gravity.BOTTOM, 0, 0)
        }
    }

    override fun dismiss() {
        super.dismiss()
        pop.dismiss()
        clickEvent?.dispose()
    }
}