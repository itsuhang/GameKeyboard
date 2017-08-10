package com.suhang.keyboard.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.suhang.keyboard.R
import com.suhang.keyboard.event.ClickEvent
import com.suhang.keyboard.event.PClickEvent
import com.suhang.keyboard.utils.KeyHelper
import com.suhang.keyboard.utils.KeyMap
import com.suhang.keyboard.widget.SelectButtonPop
import com.suhang.networkmvp.function.rx.RxBusSingle
import kotlinx.android.synthetic.main.select_button_item.view.*

/**
 * Created by 苏杭 on 2017/8/9 20:39.
 */

class SelectButtonAdapter(type: Int) : RecyclerView.Adapter<SelectButtonAdapter.Holder>() {
    val mType = type
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val text = KeyMap.keyMap.keyAt(position)
        holder.itemView.tv.text = text
        holder.itemView.setOnClickListener {
            if (mType == SelectButtonPop.STATUS_ONE) {
                RxBusSingle.instance().post(ClickEvent(text))
            } else if(mType == SelectButtonPop.STATUS_TWO){
                RxBusSingle.instance().post(PClickEvent(text))
            }
        }
        holder.itemView.setOnLongClickListener {
            Toast.makeText(holder.itemView.context, KeyHelper.getDesc(text), Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val root = View.inflate(parent.context, R.layout.select_button_item, null)
        return Holder(root)
    }

    override fun getItemCount(): Int {
        return KeyMap.keyMap.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
