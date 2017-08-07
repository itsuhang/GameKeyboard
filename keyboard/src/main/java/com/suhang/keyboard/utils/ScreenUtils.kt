package com.suhang.keyboard.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager

import java.lang.reflect.Method

/**
 * Created by sh on 2016/4/26 11:25.
 */
object ScreenUtils {
    /**
     * 获取状态栏高度
     * @param context
     * *
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen",
                "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取导航栏高度
     * @param context
     * *
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        val totalHeight = getHasVirtualKeyHeight(context)

        val contentHeight = getScreenHeight(context)

        return totalHeight - contentHeight
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度（真实高度）

     * @return
     */
    fun getHasVirtualKeyHeight(context: Context): Int {
        var dpi = 0
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val dm = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, dm)
            dpi = dm.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dpi
    }

    /**
     * 获取屏幕高度（不包括系统ui）
     * @param context
     * *
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度（不包括系统ui）
     * @param context
     * *
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕宽度（真实宽度）

     * @return
     */
    fun getHasVirtualKeyWidth(context: Context): Int {
        var dpi = 0
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val dm = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, dm)
            dpi = dm.widthPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dpi
    }


    /**
     * 判断导航栏是否显示（用户是否滑出导航栏）
     * @param context
     * *
     * @return
     */
    fun isNavigationBarShow(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y != size.y
        } else {
            val menu = ViewConfiguration.get(context).hasPermanentMenuKey()
            val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            return !(menu || back)
        }
    }

}
