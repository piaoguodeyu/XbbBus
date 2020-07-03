package com.jdsbus.util;

import com.jdsbus.util.SubscriberMethod;

import java.lang.ref.WeakReference;

/**
 * Created by zhangxiaowei on 17/4/26.
 */

class Subscription {
    WeakReference weakReference;
    SubscriberMethod subscriberMethod;
    Class clazz;

    public Subscription(Object subscriber, SubscriberMethod method, Class clazz) {
        weakReference = new WeakReference(subscriber);
        subscriberMethod = method;
        this.clazz = clazz;
    }

    Object getSubscriber() {
        return weakReference.get();
    }

    void clearSubscriber() {
        weakReference.clear();
        weakReference = null;
    }
}
