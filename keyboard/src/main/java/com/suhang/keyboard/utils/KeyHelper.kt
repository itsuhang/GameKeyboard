package com.suhang.keyboard.utils

import android.app.Instrumentation
import android.view.KeyEvent
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
        fun instance(): KeyHelper {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE: KeyHelper = KeyHelper()
    }

    val pools = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    var isShift = false
    var isCtrl = false
    var isAlt = false
    var isCap = false
    val init = Instrumentation()
    fun character(ch: Int) {
        when (ch) {
            in 48..57 -> {
                pools.execute {
                    changeChar(ch - 41)
                }
            }

            in 97..122 -> {
                pools.execute {
                    changeLetter(ch - 68)
                }
            }
        }
    }

    fun changeChar(key: Int) {
        pools.execute {
            init.sendKeyDownUpSync(key)
        }
    }

    fun changeLetter(key: Int) {
        pools.execute {
            sendKey(key)
        }
    }

    fun sendKey(key: Int) {
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, key)
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, key)
        init.sendKeySync(keyEventDown)
        init.sendKeySync(keyEventUp)
    }


    fun sendDownKey(key: Int) {
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, key)
        init.sendKeySync(keyEventDown)
    }

    fun sendUpKey(key: Int) {
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, key)
        init.sendKeySync(keyEventUp)
    }

    fun shift() {
        pools.execute {
            if (isShift) {
                sendUpKey(SHIFT_CODE)
            } else {
                sendDownKey(SHIFT_CODE)
            }
            isShift = !isShift
        }
    }

    fun capital() {
        pools.execute {
            init.sendKeyDownUpSync(CAPLOCK_CODE)
            isCap = !isCap
        }
    }

    fun ctrl() {
        pools.execute {
            if (isCtrl) {
                sendUpKey(CTRL_CODE)
            } else {
                sendDownKey(CTRL_CODE)
            }
            isCtrl = !isCtrl
        }
    }

    fun alt() {
        pools.execute {
            if (isAlt) {
                sendUpKey(ALT_CODE)
            } else {
                sendDownKey(ALT_CODE)
            }
            isAlt = !isAlt
        }
    }
}
