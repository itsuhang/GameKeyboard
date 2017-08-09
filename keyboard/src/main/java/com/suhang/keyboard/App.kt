package com.suhang.keyboard

import android.app.Application

/**
 * Created by 苏杭 on 2017/8/4 22:25.
 */
class App:Application() {
    companion object{
        lateinit var instance:App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}