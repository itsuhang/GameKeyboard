package com.suhang.keyboard.utils

import android.app.Instrumentation
import android.view.KeyEvent
import org.jetbrains.anko.AnkoLogger
import java.util.concurrent.Executors

/**
 * Created by 苏杭 on 2017/8/5 22:13.
 */

class KeyHelper private constructor() : AnkoLogger {
    companion object {
        const val SHIFT_CODE = KeyEvent.KEYCODE_SHIFT_LEFT
        const val CTRL_CODE = KeyEvent.KEYCODE_CTRL_LEFT
        const val ALT_CODE = KeyEvent.KEYCODE_ALT_LEFT
        const val CAPLOCK_CODE = KeyEvent.KEYCODE_CAPS_LOCK
        const val NUMLOCK_CODE = KeyEvent.KEYCODE_NUM_LOCK
        const val STATUS_NONE = -1
        const val STATUS_ON = 1
        const val STATUS_OFF = 0
        fun instance(): KeyHelper {
            return Holder.INSTANCE
        }


        fun getDesc(key: String): String {
            var desc = ""
            when (key) {
                "SPA" -> {
                    desc = "空格"
                }
                "N1" -> {
                    desc = "小键盘1"
                }
                "N2" -> {
                    desc = "小键盘2"
                }
                "N3" -> {
                    desc = "小键盘3"
                }
                "N4" -> {
                    desc = "小键盘4"
                }
                "N5" -> {
                    desc = "小键盘5"
                }
                "N6" -> {
                    desc = "小键盘6"
                }
                "N7" -> {
                    desc = "小键盘7"
                }
                "N8" -> {
                    desc = "小键盘8"
                }
                "N9" -> {
                    desc = "小键盘9"
                }
                "N0" -> {
                    desc = "小键盘0"
                }
                "N+" -> {
                    desc = "小键盘+"
                }
                "N-" -> {
                    desc = "小键盘-"
                }
                "N*" -> {
                    desc = "小键盘*"
                }
                "N/" -> {
                    desc = "小键盘/"
                }
                "N=" -> {
                    desc = "小键盘="
                }
                "N," -> {
                    desc = "小键盘,"
                }
                "N." -> {
                    desc = "小键盘."
                }
                "N(" -> {
                    desc = "小键盘("
                }
                "N)" -> {
                    desc = "小键盘)"
                }
                "NET" -> {
                    desc = "小键盘回车"
                }
                "ETN" -> {
                    desc = "回车"
                }
                "DEL" -> {
                    desc = "Delete键"
                }

                "BAC" -> {
                    desc = "Backspace键"
                }

                "nlc" -> {
                    desc = "小键盘锁"
                }
                "cap" -> {
                    desc = "大小写锁"
                }
                "INS" -> {
                    desc = "Insert键"
                }
                "PUP" -> {
                    desc = "PgUp键"
                }
                "PDW" -> {
                    desc = "PgDn键"
                }
                "HOM" -> {
                    desc = "Home键"
                }
                "all" -> {
                    desc = "Alt锁"
                }
                "shl" -> {
                    desc = "Shift锁"
                }
                "ctl" -> {
                    desc = "Ctrl锁"
                }
                "ALT" -> {
                    desc = "单击Alt键"
                }
                "SHT" -> {
                    desc = "单击Shift"
                }
                "CTR" -> {
                    desc = "单击Ctrl"
                }
                "KEY" -> {
                    desc = "唤起软键盘"
                }

            }
            return desc
        }
    }

    private object Holder {
        val INSTANCE: KeyHelper = KeyHelper()
    }

    private val pools = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private var isShift = false
    private var isCtrl = false
    private var isAlt = false
    private var isCap = false
    private var isNum = false
    private val init = Instrumentation()

    fun send(ch: String):Int {
        val letter = ch.toUpperCase()
        var key = KeyMap.keyMap[ch]
        if (key == null) {
            key = KeyMap.keySpecialMap[letter]
        }
        when (key) {
            KeyEvent.KEYCODE_SHIFT_LEFT -> {
                return shift(ch)
            }
            KeyEvent.KEYCODE_ALT_LEFT -> {
                return alt(ch)
            }
            KeyEvent.KEYCODE_CTRL_LEFT -> {
                return ctrl(ch)
            }

            KeyEvent.KEYCODE_CAPS_LOCK -> {
                capital()
                return if(isCap) STATUS_ON else STATUS_OFF
            }

            KeyEvent.KEYCODE_NUM_LOCK -> {
                numlock()
                return if(isNum) STATUS_ON else STATUS_OFF
            }

            else -> {
                sendKey(key)
            }
        }
        return STATUS_NONE
    }

    fun sendKey(key: Int?) {
        if (key != null) {
            pools.execute {
                try {
                    init.sendKeyDownUpSync(key)
                } catch (e: SecurityException) {
                }
            }
        }
    }


    fun sendDownKey(key: Int) {
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, key)
        pools.execute {
            try {
                init.sendKeySync(keyEventDown)
            } catch (e: SecurityException) {
            }
        }
    }

    fun sendUpKey(key: Int) {
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, key)
        pools.execute {
            try {
                init.sendKeySync(keyEventUp)
            } catch (e: SecurityException) {
            }
        }
    }

    fun shift(key: String):Int {
        val k = key.toLowerCase()
        when (k) {
            "sht" -> {
                sendKey(SHIFT_CODE)
            }
            "shl" -> {
                if (isShift) {
                    sendUpKey(SHIFT_CODE)
                } else {
                    sendDownKey(SHIFT_CODE)
                }
                isShift = !isShift
                return if(isShift) STATUS_ON else STATUS_OFF
            }
        }
        return STATUS_NONE
    }

    fun capital() {
        sendKey(CAPLOCK_CODE)
        isCap = !isCap
    }

    fun numlock() {
        sendKey(NUMLOCK_CODE)
        isNum = !isNum
    }

    fun ctrl(key: String) :Int{
        val k = key.toLowerCase()
        when (k) {
            "ctr" -> {
                sendKey(CTRL_CODE)
            }
            "ctl" -> {
                if (isCtrl) {
                    sendUpKey(CTRL_CODE)
                } else {
                    sendDownKey(CTRL_CODE)
                }
                isCtrl = !isCtrl
                return if(isCtrl) STATUS_ON else STATUS_OFF
            }
        }
        return STATUS_NONE
    }

    fun alt(key: String):Int {
        val k = key.toLowerCase()
        when (k) {
            "alt" -> {
                sendKey(ALT_CODE)
            }
            "all" -> {
                if (isAlt) {
                    sendUpKey(ALT_CODE)
                } else {
                    sendDownKey(ALT_CODE)
                }
                isAlt = !isAlt
                return if(isAlt) STATUS_ON else STATUS_OFF
            }
        }
        return STATUS_NONE
    }

}
