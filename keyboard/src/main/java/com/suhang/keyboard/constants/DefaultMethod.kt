package com.suhang.keyboard.constants
import android.view.View

/**
 * Created by 苏杭 on 2017/5/23 17:30.
 */

fun View.dip2px(dpValue: Float): Int {
    return (resources.displayMetrics.density * dpValue + 0.5f).toInt()
}

fun View.dip2pxf(dpValue: Float): Float {
    return resources.displayMetrics.density * dpValue + 0.5f
}

fun View.px2dip(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.density + 0.5f).toInt()
}

fun View.px2dipf(pxValue: Float): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}



