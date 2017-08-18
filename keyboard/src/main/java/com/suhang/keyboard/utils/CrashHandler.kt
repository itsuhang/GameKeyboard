package com.suhang.networkmvp.function

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.util.Log
import com.suhang.keyboard.App
import java.io.*
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat
import java.util.*

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 *
 * Created by sh on 16-7-13
 */
class CrashHandler private constructor() : UncaughtExceptionHandler {

    //系统默认的UncaughtException处理类
    private var mDefaultHandler: UncaughtExceptionHandler? = null
    //程序的Context对象
    lateinit var mContext: Context
    //用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()

    //用于格式化日期,作为日志文件名的一部分
    private val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

    /**
     * 初始化

     * @param context
     */
    fun init(context: Context) {
        mContext = context
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.

     * @param ex
     * *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        //收集设备参数信息
        collectDeviceInfo(mContext)
        //保存日志文件
        saveCrashInfo2File(ex)
        return true
    }

    /**
     * 收集设备参数信息

     * @param ctx
     */
    fun collectDeviceInfo(ctx: Context) {
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                infos.put("versionName", versionName)
                infos.put("versionCode", versionCode)
            }
        } catch (e: NameNotFoundException) {
            Log.e(TAG, "收集包信息时发生错误", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos.put(field.name, field.get(null).toString())
                Log.d(TAG, field.name + " : " + field.get(null))
            } catch (e: Exception) {
                Log.e(TAG, "收集信息时发生错误", e)
            }

        }
    }

    private val divider = "---------------------------------------------------------------"

    /**
     * 保存错误信息到文件中

     * @param ex
     * *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable) {

        val sb = StringBuffer()
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        val dir = File(App.APP_PATH)
        val file = File(dir, "log.txt")
        if (!dir.exists()) {
            dir.mkdir()
        }
        try {
            if (!file.exists()) {
                val success = file.createNewFile()
                if (success) {
                    val fileWriter = FileWriter(file)
                    fileWriter.write(sb.toString())
                    fileWriter.flush()
                    fileWriter.close()
                }
            } else {
                val fileWriter = FileWriter(file)
                fileWriter.write(sb.toString())
                fileWriter.flush()
                fileWriter.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {

        val TAG = "CrashHandler"
        //CrashHandler实例
        /**
         * 获取CrashHandler实例 ,单例模式
         */
        val instance = CrashHandler()
    }
}