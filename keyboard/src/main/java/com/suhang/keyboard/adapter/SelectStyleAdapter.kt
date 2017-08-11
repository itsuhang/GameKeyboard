package com.suhang.keyboard.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.suhang.keyboard.R
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.event.SelectFileEvent
import com.suhang.keyboard.utils.GsonUtil
import com.suhang.networkmvp.function.rx.RxBusSingle
import kotlinx.android.synthetic.main.selectstyle_dialog_item.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * Created by 苏杭 on 2017/8/11 11:49.
 */
class SelectStyleAdapter(context: Context) : RecyclerView.Adapter<SelectStyleAdapter.Holder>(),AnkoLogger {
    var mFileList = ArrayList<File>()
    val mContext = context
    override fun getItemCount(): Int {
        return mFileList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        info(mFileList[position].name)
        holder.itemView.txt_style.text = mFileList[position].name
        holder.itemView.setOnClickListener {
            val list = getFileContent(mFileList[position])
            list?.let {
                RxBusSingle.instance().post(SelectFileEvent(it))
            }
        }
    }

    fun refresh(fileList: ArrayList<File>) {
        mFileList = fileList
        notifyDataSetChanged()
    }


    fun getFileContent(file: File): ArrayList<ButtonData>? {
        try {
            val reader = BufferedReader(FileReader(file))
            val text = reader.readText()
            reader.close()
            val data = GsonUtil.getData(text)
            return data
        } catch (e: Exception) {
            Toast.makeText(mContext,"读取样式失败",Toast.LENGTH_SHORT).show()
            return null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = View.inflate(parent.context, R.layout.selectstyle_dialog_item, null)
        return Holder(view)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}