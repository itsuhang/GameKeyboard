package com.suhang.keyboard.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.suhang.keyboard.R
import com.suhang.keyboard.event.ClickEvent
import com.suhang.keyboard.utils.KeyHelper
import com.suhang.keyboard.utils.KeyMap
import com.suhang.networkmvp.function.rx.RxBusSingle
import kotlinx.android.synthetic.main.select_button_item.view.*

/**
 * Created by 苏杭 on 2017/8/9 20:39.
 */

class SelectButtonAdapter: RecyclerView.Adapter<SelectButtonAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val text = KeyMap.keyMap.keyAt(position)
        holder.itemView.tv.text = text
        holder.itemView.setOnClickListener {
            RxBusSingle.instance().post(ClickEvent(text))
        }
        holder.itemView.setOnLongClickListener {
            Toast.makeText(holder.itemView.context,KeyHelper.getDesc(text),Toast.LENGTH_SHORT).show()
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

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView)
}
