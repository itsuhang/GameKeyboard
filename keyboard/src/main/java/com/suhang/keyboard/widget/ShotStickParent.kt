package com.suhang.keyboard.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.widget.FrameLayout
import com.suhang.keyboard.FloatKeyboard
import com.suhang.keyboard.R
import com.suhang.keyboard.data.ButtonData
import com.suhang.keyboard.utils.KeyHelper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

/**
 * Created by 苏杭 on 2017/8/14 12:06.
 */
class ShotStickParent : FrameLayout, AnkoLogger {
    private var externalView: ShotStick = ShotStick(context)
    private var innerView: ShotStick = ShotStick(context)
    private val thresholdValue1 = Math.sin((22.5 * Math.PI) / 180)
    private val thresholdValue2 = Math.sin((67.5 * Math.PI) / 180)
    private var direction = Direction.CENTER
    private var dispose: Disposable? = null
    private lateinit var data: ButtonData
    override fun setBackgroundColor(color: Int) {
        externalView.setBackgroundColor(color)
        innerView.setBackgroundColor(color)
    }
    enum class Direction {
        CENTER, RIGHT, LEFT, UP, DOWN, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT
    }

    override fun setTag(key: Int, tag: Any?) {
        super.setTag(key, tag)
        if (key == R.id.data) {
            info(tag)
            data = tag as ButtonData
            createTask()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var lastDirection = Direction.CENTER

    init {
        val externalParam = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        externalView.layoutParams = externalParam
        innerView.isStroke = false
        addView(externalView)
        addView(innerView)
        super.setBackgroundColor(R.color.little_gray)
        post({
            val param = innerView.layoutParams as FrameLayout.LayoutParams
            innerView.layoutParams.width = width / 3
            innerView.layoutParams.height = height / 3
            param.gravity = Gravity.CENTER
            innerView.requestLayout()
        })
        externalView.setOnTouchListener { _, event ->
            val centerX = 1.0f * width / 2
            val centerY = 1.0f * height / 2
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    listener?.onStatusChanger(direction)
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = event.x
                    val y = event.y
                    var dx = x - centerX
                    var dy = centerY - y
                    val radius = 1.0f * width / 2 - 1.0f * width / 6
                    val slope = Math.sqrt(((dx * dx + dy * dy).toDouble()))
                    if (Math.abs(dx) > width / 6 || Math.abs(dy) > height / 6) {
                        val proportion = radius / slope
                        if (slope >= radius) {
                            dx = (dx * proportion).toFloat()
                            dy = (dy * proportion).toFloat()
                        }
                        innerView.translationX = dx
                        innerView.translationY = -dy
                        val ratio = dy / slope
                        if (ratio > -thresholdValue1 && ratio < thresholdValue1 && dx > 0) {
                            direction = Direction.RIGHT
                        } else if (ratio > -thresholdValue1 && ratio < thresholdValue1 && dx < 0) {
                            direction = Direction.LEFT
                        } else if (ratio > thresholdValue2 && ratio < 1 && dy > 0) {
                            direction = Direction.UP
                        } else if (ratio < -thresholdValue2 && ratio > -1 && dy < 0) {
                            direction = Direction.DOWN
                        } else if (ratio in thresholdValue1..thresholdValue2 && dx > 0) {
                            direction = Direction.UP_RIGHT
                        } else if (ratio in thresholdValue1..thresholdValue2 && dx < 0) {
                            direction = Direction.UP_LEFT
                        } else if (ratio >= -thresholdValue2 && ratio <= -thresholdValue1 && dx > 0) {
                            direction = Direction.DOWN_RIGHT
                        } else if (ratio >= -thresholdValue2 && ratio <= -thresholdValue1 && dx < 0) {
                            direction = Direction.DOWN_LEFT
                        }
                        if (direction != lastDirection) {
                            listener?.onStatusChanger(direction)
                        }
                        lastDirection = direction
                    }
                }
                MotionEvent.ACTION_UP -> {
                    innerView.translationX = 0f
                    innerView.translationY = 0f
                    direction = Direction.CENTER
                }
                MotionEvent.ACTION_CANCEL -> {
                    innerView.translationX = 0f
                    innerView.translationY = 0f
                    direction = Direction.CENTER
                }
            }
            !FloatKeyboard.isEdit
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dispose?.dispose()
    }

    fun createTask() {
        dispose = Flowable.interval(data.speed, data.speed, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe({
            when (direction) {
                Direction.CENTER->{

                }
                Direction.RIGHT -> {
                    KeyHelper.instance().send("→")
                }
                Direction.LEFT -> {
                    KeyHelper.instance().send("←")
                }

                Direction.UP -> {
                    KeyHelper.instance().send("↑")
                }

                Direction.DOWN -> {
                    KeyHelper.instance().send("↓")
                }

                Direction.UP_LEFT -> {
                    KeyHelper.instance().send("↖")
                }

                Direction.UP_RIGHT -> {
                    KeyHelper.instance().send("↗")
                }

                Direction.DOWN_LEFT -> {
                    KeyHelper.instance().send("↙")
                }
                Direction.DOWN_RIGHT -> {
                    KeyHelper.instance().send("↘")
                }
            }
        })
    }

    private var listener: OnStatusChangedLisenter? = null
    fun setOnStatusChangedListener(listener: OnStatusChangedLisenter) {
        this.listener = listener
    }

    interface OnStatusChangedLisenter {
        fun onStatusChanger(direction: Direction)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = MeasureSpec.makeMeasureSpec(width, wMode)
        Math.min(width, height)
        super.onMeasure(size, size)
    }
}