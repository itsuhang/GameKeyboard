package com.suhang.keyboard.data


/**
 * Created by 苏杭 on 2017/8/8 21:29.
 */

data class ButtonData(var key:String,var width: Int, var height: Int, var x: Int, var y: Int,var fontSize:Int,var color:Int,var alpha:Float,var shape:Int,var speed:Long){
    companion object{
        const val CIRCLE = 101
        const val SQUARE = 102
    }
}

