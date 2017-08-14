package com.suhang.keyboard.utils

import android.util.ArrayMap
import android.view.KeyEvent

/**
 * Created by 苏杭 on 2017/8/7 20:52.
 */
class KeyMap {
    companion object {
        val keyMap = ArrayMap<String, Int>()
        val keySpecialMap = ArrayMap<String, Int>()
        const val KEYBOARD = 9999
        const val UP_LEFT = 9998
        const val UP_RIGHT = 9997
        const val DOWN_LEFT = 9996
        const val DOWN_RIGHT = 9995
        const val MANAGER = 10000
        const val MANAGER_RETURN_CODE = 10001
        const val MANAGER_STICK_CODE = 10002
        const val MANAGER_ST = "●"
        const val MANAGER_BACK = "◁"
        const val MANAGER_HOME = "〇"
        const val MANAGER_RETURN = "☢"
        const val MANAGER_STICK = "摇杆"

        fun isSpecalKey(key:String): Boolean {
            val letter = key.toLowerCase()
            when (letter) {
                MANAGER_ST->{
                    return true
                }
                "shl"->{
                    return true
                }
                "ctl"->{
                    return true
                }
                "all"->{
                    return true
                }
                "cap"->{
                    return true
                }
                "nlc"->{
                    return true
                }
                "key"->{
                    return true
                }
            }
            return false
        }
        init {
            keyMap.run {
                put("a", KeyEvent.KEYCODE_A)
                put("b", KeyEvent.KEYCODE_B)
                put("c", KeyEvent.KEYCODE_C)
                put("d", KeyEvent.KEYCODE_D)
                put("e", KeyEvent.KEYCODE_E)
                put("f", KeyEvent.KEYCODE_F)
                put("g", KeyEvent.KEYCODE_G)
                put("h", KeyEvent.KEYCODE_H)
                put("i", KeyEvent.KEYCODE_I)
                put("j", KeyEvent.KEYCODE_J)
                put("k", KeyEvent.KEYCODE_K)
                put("l", KeyEvent.KEYCODE_L)
                put("m", KeyEvent.KEYCODE_M)
                put("n", KeyEvent.KEYCODE_N)
                put("o", KeyEvent.KEYCODE_O)
                put("p", KeyEvent.KEYCODE_P)
                put("q", KeyEvent.KEYCODE_Q)
                put("r", KeyEvent.KEYCODE_R)
                put("s", KeyEvent.KEYCODE_S)
                put("t", KeyEvent.KEYCODE_T)
                put("u", KeyEvent.KEYCODE_U)
                put("v", KeyEvent.KEYCODE_V)
                put("w", KeyEvent.KEYCODE_W)
                put("x", KeyEvent.KEYCODE_X)
                put("y", KeyEvent.KEYCODE_Y)
                put("z", KeyEvent.KEYCODE_Z)
                put("A", KeyEvent.KEYCODE_A)
                put("B", KeyEvent.KEYCODE_B)
                put("C", KeyEvent.KEYCODE_C)
                put("D", KeyEvent.KEYCODE_D)
                put("E", KeyEvent.KEYCODE_E)
                put("F", KeyEvent.KEYCODE_F)
                put("G", KeyEvent.KEYCODE_G)
                put("H", KeyEvent.KEYCODE_H)
                put("I", KeyEvent.KEYCODE_I)
                put("J", KeyEvent.KEYCODE_J)
                put("K", KeyEvent.KEYCODE_K)
                put("L", KeyEvent.KEYCODE_L)
                put("M", KeyEvent.KEYCODE_M)
                put("N", KeyEvent.KEYCODE_N)
                put("O", KeyEvent.KEYCODE_O)
                put("P", KeyEvent.KEYCODE_P)
                put("Q", KeyEvent.KEYCODE_Q)
                put("R", KeyEvent.KEYCODE_R)
                put("S", KeyEvent.KEYCODE_S)
                put("T", KeyEvent.KEYCODE_T)
                put("U", KeyEvent.KEYCODE_U)
                put("V", KeyEvent.KEYCODE_V)
                put("W", KeyEvent.KEYCODE_W)
                put("X", KeyEvent.KEYCODE_X)
                put("Y", KeyEvent.KEYCODE_Y)
                put("Z", KeyEvent.KEYCODE_Z)
                put("1", KeyEvent.KEYCODE_1)
                put("2", KeyEvent.KEYCODE_2)
                put("3", KeyEvent.KEYCODE_3)
                put("4", KeyEvent.KEYCODE_4)
                put("5", KeyEvent.KEYCODE_5)
                put("6", KeyEvent.KEYCODE_6)
                put("7", KeyEvent.KEYCODE_7)
                put("8", KeyEvent.KEYCODE_8)
                put("9", KeyEvent.KEYCODE_9)
                put("0", KeyEvent.KEYCODE_0)
                put("@", KeyEvent.KEYCODE_AT)
                put("#", KeyEvent.KEYCODE_POUND)
                put("*", KeyEvent.KEYCODE_STAR)
                put("+", KeyEvent.KEYCODE_PLUS)
                put("-", KeyEvent.KEYCODE_MINUS)
                put("=", KeyEvent.KEYCODE_EQUALS)
                put("/", KeyEvent.KEYCODE_BACKSLASH)
                put(",", KeyEvent.KEYCODE_COMMA)
                put(".", KeyEvent.KEYCODE_PERIOD)
                put("[", KeyEvent.KEYCODE_LEFT_BRACKET)
                put("]", KeyEvent.KEYCODE_RIGHT_BRACKET)
                put(";", KeyEvent.KEYCODE_PERIOD)
                put("'", KeyEvent.KEYCODE_PERIOD)
                put("N1", KeyEvent.KEYCODE_NUMPAD_1)
                put("N2", KeyEvent.KEYCODE_NUMPAD_2)
                put("N3", KeyEvent.KEYCODE_NUMPAD_3)
                put("N4", KeyEvent.KEYCODE_NUMPAD_4)
                put("N5", KeyEvent.KEYCODE_NUMPAD_5)
                put("N6", KeyEvent.KEYCODE_NUMPAD_6)
                put("N7", KeyEvent.KEYCODE_NUMPAD_7)
                put("N8", KeyEvent.KEYCODE_NUMPAD_8)
                put("N9", KeyEvent.KEYCODE_NUMPAD_9)
                put("N0", KeyEvent.KEYCODE_NUMPAD_0)


                put("N+", KeyEvent.KEYCODE_NUMPAD_ADD)
                put("N-", KeyEvent.KEYCODE_NUMPAD_SUBTRACT)
                put("N*", KeyEvent.KEYCODE_NUMPAD_MULTIPLY)
                put("N/", KeyEvent.KEYCODE_NUMPAD_DIVIDE)
                put("N=", KeyEvent.KEYCODE_NUMPAD_EQUALS)
                put("N,", KeyEvent.KEYCODE_NUMPAD_COMMA)
                put("N.", KeyEvent.KEYCODE_NUMPAD_DOT)
                put("N(", KeyEvent.KEYCODE_NUMPAD_LEFT_PAREN)
                put("N)", KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN)
                put("NET", KeyEvent.KEYCODE_NUMPAD_ENTER)
                put("F1", KeyEvent.KEYCODE_F1)
                put("F2", KeyEvent.KEYCODE_F2)
                put("F3", KeyEvent.KEYCODE_F3)
                put("F4", KeyEvent.KEYCODE_F4)
                put("F5", KeyEvent.KEYCODE_F5)
                put("F6", KeyEvent.KEYCODE_F6)
                put("F7", KeyEvent.KEYCODE_F7)
                put("F8", KeyEvent.KEYCODE_F8)
                put("F9", KeyEvent.KEYCODE_F9)
                put("F10", KeyEvent.KEYCODE_F10)
                put("F11", KeyEvent.KEYCODE_F11)
                put("F12", KeyEvent.KEYCODE_F12)


                put("SPA", KeyEvent.KEYCODE_SPACE)
                put("ETN", KeyEvent.KEYCODE_ENTER)
                put("ESC", KeyEvent.KEYCODE_ESCAPE)
                put("↑", KeyEvent.KEYCODE_DPAD_UP)
                put("↓", KeyEvent.KEYCODE_DPAD_DOWN)
                put("←", KeyEvent.KEYCODE_DPAD_LEFT)
                put("→", KeyEvent.KEYCODE_DPAD_RIGHT)
                put("↖", UP_LEFT)
                put("↗", UP_RIGHT)
                put("↙", DOWN_LEFT)
                put("↘", DOWN_RIGHT)
                put("DEL", KeyEvent.KEYCODE_DEL)
                put("BAC", KeyEvent.KEYCODE_FORWARD_DEL)
                //键盘锁
                put("nlc", KeyEvent.KEYCODE_NUM_LOCK)
                //大小写锁
                put("cap", KeyEvent.KEYCODE_CAPS_LOCK)
                put("TAB", KeyEvent.KEYCODE_TAB)
                put("INS", KeyEvent.KEYCODE_INSERT)
                put("PUP", KeyEvent.KEYCODE_PAGE_UP)
                put("PDW", KeyEvent.KEYCODE_PAGE_DOWN)
                put("HOM", KeyEvent.KEYCODE_MOVE_HOME)
                put("END", KeyEvent.KEYCODE_MOVE_END)
                put("ALT", KeyEvent.KEYCODE_ALT_LEFT)
                put("all", KeyEvent.KEYCODE_ALT_LEFT)
                put("SHT", KeyEvent.KEYCODE_SHIFT_LEFT)
                put("shl", KeyEvent.KEYCODE_SHIFT_LEFT)
                put("CTR", KeyEvent.KEYCODE_CTRL_LEFT)
                put("ctl", KeyEvent.KEYCODE_CTRL_LEFT)
                put("KEY", KEYBOARD)
                put(MANAGER_ST, MANAGER)
                put(MANAGER_STICK, MANAGER_STICK_CODE)
                put(MANAGER_BACK, KeyEvent.KEYCODE_BACK)
                put(MANAGER_HOME, KeyEvent.KEYCODE_HOME)
                put(MANAGER_RETURN, MANAGER_RETURN_CODE)
            }
            keySpecialMap.run {
                put("NLC", KeyEvent.KEYCODE_NUM_LOCK)
                put("ALL", KeyEvent.KEYCODE_ALT_LEFT)
                put("CAP", KeyEvent.KEYCODE_CAPS_LOCK)
                put("SHL", KeyEvent.KEYCODE_SHIFT_LEFT)
                put("CTL", KeyEvent.KEYCODE_CTRL_LEFT)
            }
        }
    }
}