package com.jdsbus;

/**
 * Created by zhangxiaowei on 17/4/26.
 */

class Subscription {
    Object subscriber;
     SubscriberMethod subscriberMethod;

    public Subscription(Object subscriber, SubscriberMethod method) {
        this.subscriber = subscriber;
        subscriberMethod=method;
    }

}
