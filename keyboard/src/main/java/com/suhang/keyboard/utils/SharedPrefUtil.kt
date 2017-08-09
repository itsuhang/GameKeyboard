package com.suhang.keyboard.utils

import android.content.Context
import android.content.SharedPreferences
import com.suhang.keyboard.App


/**
 * 本地存储SharedPreferences工具
 * Created by sh
 */
object SharedPrefUtil {
    lateinit var mSp: SharedPreferences
    private var name = "config"

    private fun getSharedPref(context: Context): SharedPreferences {
        mSp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return mSp
    }

    /**
     * 设置SharePrefrences文件名字

     * @param name
     */
    fun setName(name: String) {
        SharedPrefUtil.name = name
    }


    //下面方法都是对不同数据类型进行保存,获取

    fun putBoolean(key: String, value: Boolean) {
        getSharedPref(App.instance).edit()?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return getSharedPref(App.instance).getBoolean(key, defValue)
    }

    fun putString(key: String, value: String) {
        getSharedPref(App.instance).edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String, defValue: String): String {
        return getSharedPref(App.instance).getString(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        getSharedPref(App.instance).edit()?.putInt(key, value)?.apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return getSharedPref(App.instance).getInt(key, defValue)
    }

    fun putFloat(key: String, value: Float) {
        getSharedPref(App.instance).edit()?.putFloat(key, value)?.apply()
    }

    fun getFloat(key: String, defValue: Float): Float {
        return getSharedPref(App.instance).getFloat(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        getSharedPref(App.instance).edit()?.putLong(key, value)?.apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return getSharedPref(App.instance).getLong(key, defValue)
    }


    /**
     * 删除某个键值对

     * @param context
     * *
     * @param key
     */
    fun remove(context: Context, key: String) {
        getSharedPref(context).edit()?.remove(key)?.apply()
    }

}