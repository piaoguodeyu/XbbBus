package com.jdsbus;

import android.arch.lifecycle.LifecycleOwner;

import com.jdsbus.interfacs.PostMessage;
import com.jdsbus.interfacs.Register;
import com.jdsbus.lifecycler.JdsBusLifeCycler;
import com.jdsbus.lifecycler.LifeCyclerObserver;

/**
 * Created by zhangxiaowei on 17/4/25.
 */

public class JdsBus implements PostMessage {

    private static volatile JdsBus mHjdsBus;


    public static JdsBus getDefaut() {
        if (mHjdsBus == null) {
            synchronized (JdsBus.class) {
                if (mHjdsBus == null) {
                    mHjdsBus = new JdsBus();
                }
            }
        }
        return mHjdsBus;
    }

    private JdsBus() {

    }

    public static Register bindLifeCycler(JdsBusLifeCycler lifeCycler, LifecycleOwner lifecycleOwner) {
        return new LifeCyclerObserver(lifeCycler, lifecycleOwner);
    }

    public static Register bindOnDestroyLifeCycler(LifecycleOwner lifecycleOwner) {
        return new LifeCyclerObserver(JdsBusLifeCycler.onDestroy, lifecycleOwner);
    }

    public void register(Object subscriber) {
    }

    /**
     * 发送给指定的订阅者
     *
     * @param data
     * @param clazz
     */
    @Override
    public void post(Object data, Class<?> clazz) {

    }

    /**
     * 发送给所有订阅者
     *
     * @param data
     */
    @Override
    public void post(Object data) {
        post(data, null);
    }


    /**
     * 解订阅
     *
     * @param subscriber
     */
    public void unRegister(Object subscriber) {

    }


}
