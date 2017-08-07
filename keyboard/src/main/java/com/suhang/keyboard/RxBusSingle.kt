package com.suhang.networkmvp.function.rx

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.subscribers.SerializedSubscriber

class RxBusSingle {
    private val mBus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    companion object {
        fun instance(): RxBusSingle {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE: RxBusSingle = RxBusSingle()
    }

    /**
     * 发送消息
     * @param o
     */
    fun post(o: Any) {
        SerializedSubscriber(mBus).onNext(o)
    }

    /**
     * 确定接收消息的类型
     * @param aClass
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> toFlowable(aClass: Class<T>): Flowable<T> {
        return mBus.ofType(aClass)
    }

    /**
     * 判断是否有订阅者
     * @return
     */
    fun hasSubscribers(): Boolean {
        return mBus.hasSubscribers()
    }

}