package com.suhang.keyboard.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.suhang.keyboard.R
import com.suhang.keyboard.utils.KeyMap
import kotlinx.android.synthetic.main.select_button_item.view.*
import java.lang.StringBuilder

/**
 * Created by 苏杭 on 2017/8/9 20:39.
 */

class GroupButtonAdapter : RecyclerView.Adapter<GroupButtonAdapter.Holder>() {
    private val list = ArrayList<String>()
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val text = KeyMap.keyMap.keyAt(position)
        holder.itemView.tv.text = text
        if (list.contains(text)) {
            holder.itemView.tv.setBackgroundColor(holder.itemView.resources.getColor(R.color.blue))
        } else {
            holder.itemView.tv.setBackgroundColor(holder.itemView.resources.getColor(R.color.white))
        }
        holder.itemView.setOnClickListener {
            if (KeyMap.isSpecalKey(text)) {
                Toast.makeText(holder.itemView.context, "不能选择该按键", Toast.LENGTH_SHORT).show()
            } else {
                when(text){
                    KeyMap.MANAGER_SOFT->{
                        Toast.makeText(holder.itemView.context,"不能选择该按键",Toast.LENGTH_SHORT).show()
                    }
//                    KeyMap.MANAGER_RETURN->{
//                        Toast.makeText(holder.itemView.context,"不能选择该按键",Toast.LENGTH_SHORT).show()
//                    }
                    KeyMap.MANAGER_HOME->{
                        Toast.makeText(holder.itemView.context,"不能选择该按键",Toast.LENGTH_SHORT).show()
                    }
                    KeyMap.MANAGER_STICK->{
                        Toast.makeText(holder.itemView.context,"不能选择该按键",Toast.LENGTH_SHORT).show()
                    }
                    KeyMap.MANAGER_BACK->{
                        Toast.makeText(holder.itemView.context,"不能选择该按键",Toast.LENGTH_SHORT).show()
                    }
                    KeyMap.MANAGER_ST->{
                        Toast.makeText(holder.itemView.context,"不能选择该按键",Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        if (list.contains(text)) {
                            holder.itemView.tv.setBackgroundColor(holder.itemView.resources.getColor(R.color.white))
                            list.remove(text)
                        } else {
                            holder.itemView.tv.setBackgroundColor(holder.itemView.resources.getColor(R.color.blue))
                            list.add(text)
                        }
                    }
                }
            }
        }
    }

    fun getKey():String?{
        if (list.size <=1) {
            return null
        }
        val sb = StringBuilder()
        list.forEachIndexed { index, s ->
            if (index != 0) {
                sb.append("%%%$s")
            } else {
                sb.append(s)
            }
        }
        return sb.toString()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
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
