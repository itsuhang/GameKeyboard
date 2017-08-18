package com.suhang.keyboard.data


/**
 * Created by 苏杭 on 2017/8/8 21:29.
 */

data class ButtonData(var key: String = "key", var width: Int = 100, var height: Int = 100, var x: Int = 0, var y: Int = 0, var fontSize: Int = 12, var color: Int= 0x666666, var alpha: Float = 1.0f, var shape: Int = SQUARE, var speed: Long = 100, var combinationKey: String? = "") {
    companion object {
        const val CIRCLE = 101
        const val SQUARE = 102
    }
}

