package com.suhang.keyboard

import android.app.Application
import android.os.Environment
import com.suhang.networkmvp.function.CrashHandler

/**
 * Created by 苏杭 on 2017/8/4 22:25.
 */
class App:Application() {
    companion object{
        lateinit var instance:App
        var APP_PATH = "${Environment.getExternalStorageDirectory().absolutePath}/suhang"
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashHandler.instance.init(this)
    }
}