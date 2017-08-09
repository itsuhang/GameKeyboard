package com.suhang.keyboard.utils

import android.app.Instrumentation
import android.view.KeyEvent
import com.suhang.keyboard.FloatService
import com.suhang.keyboard.event.KeyboardEvent
import com.suhang.networkmvp.function.rx.RxBusSingle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
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

    fun send(ch: String) {
        val letter = ch.toUpperCase()
        var key = KeyMap.keyMap["aaa"]
        if (key == null) {
            key = KeyMap.keySpecialMap[letter]
        }
        when (key) {
            KeyEvent.KEYCODE_SHIFT_LEFT -> {
                shift(ch)
            }
            KeyEvent.KEYCODE_ALT_LEFT -> {
                alt(ch)
            }
            KeyEvent.KEYCODE_CTRL_LEFT -> {
                ctrl(ch)
            }

            KeyEvent.KEYCODE_CAPS_LOCK -> {
                capital()
            }

            KeyEvent.KEYCODE_NUM_LOCK -> {
                numlock()
            }

            else -> {
                sendKey(key)
            }
        }
    }

    fun sendKey(key: Int?) {
        if (key != null) {
            pools.execute {
                init.sendKeyDownUpSync(key)
            }
        }
    }


    fun sendDownKey(key: Int) {
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, key)
        pools.execute {
            init.sendKeySync(keyEventDown)
        }
    }

    fun sendUpKey(key: Int) {
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, key)
        pools.execute {
            init.sendKeySync(keyEventUp)
        }
    }

    fun shift(key: String) {
        when (key) {
            "SHT" -> {
                sendKey(SHIFT_CODE)
            }
            "SHL" -> {
                if (isShift) {
                    sendUpKey(SHIFT_CODE)
                } else {
                    sendDownKey(SHIFT_CODE)
                }
                isShift = !isShift
            }
        }
    }

    fun capital() {
        sendKey(CAPLOCK_CODE)
        isCap = !isCap
    }

    fun numlock() {
        sendKey(NUMLOCK_CODE)
        isNum = !isNum
    }

    fun ctrl(key: String) {
        when (key) {
            "CTR" -> {
                sendKey(CTRL_CODE)
            }
            "CTL" -> {
                if (isCtrl) {
                    sendUpKey(CTRL_CODE)
                } else {
                    sendDownKey(CTRL_CODE)
                }
                isCtrl = !isCtrl
            }
        }
    }

    fun alt(key: String) {
        when (key) {
            "ALT" -> {
                sendKey(ALT_CODE)
            }
            "ALL" -> {
                if (isAlt) {
                    sendUpKey(ALT_CODE)
                } else {
                    sendDownKey(ALT_CODE)
                }
                isAlt = !isAlt
            }
        }
    }

}
