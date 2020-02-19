package com.xbbbus;

import android.os.Looper;
import android.os.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhangxiaowei on 17/4/25.
 */

public class XbbBus {

    private static volatile XbbBus mMitakeBus;
    SubscriberMethodFinder mMethodFinder;
    ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Subscription>> mSubscription;
    HandlerUtil mHandler;

    public static XbbBus getDefaut() {
        if (mMitakeBus == null) {
            synchronized (XbbBus.class) {
                if (mMitakeBus == null) {
                    mMitakeBus = new XbbBus();
                }
            }
        }
        return mMitakeBus;
    }

    private XbbBus() {
        mMethodFinder = new SubscriberMethodFinder();
        mSubscription = new ConcurrentHashMap<>();
        mHandler = new HandlerUtil(Looper.getMainLooper());
    }

    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        CopyOnWriteArrayList<Subscription> subscriptions = mSubscription.get(clazz);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
        }
        List<SubscriberMethod> subscriberMethods = mMethodFinder.getMitakeMethod(clazz);
        if (!subscriberMethods.isEmpty()) {
            for (SubscriberMethod method : subscriberMethods) {
                Subscription subscription = new Subscription(subscriber, method);
                subscriptions.add(subscription);
            }
            mSubscription.put(clazz, subscriptions);
        }

    }

    /**
     * 发送给指定的订阅者
     *
     * @param data
     * @param clazz
     */
    public void post(Object data, Class<?> clazz) {
        try {
            PostSigleData sigleData = new PostSigleData(data, clazz);
            postToSubscripber(sigleData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送给所有订阅者
     *
     * @param data
     */
    public void post(Object data) {
        post(data, null);
    }

    /**
     * 发送给指定订阅者
     */
    private void postToSubscripber(PostSigleData data) {

        if (data.sigleClazz == null) {
            for (Map.Entry<Class<?>, CopyOnWriteArrayList<Subscription>> map : mSubscription.entrySet()) {
                CopyOnWriteArrayList<Subscription> list = map.getValue();
                if (list == null) {
                    return;
                }
                invokeData(data, list);
            }
        } else {
            CopyOnWriteArrayList<Subscription> list = mSubscription.get(data.sigleClazz);
            if (list == null) {
                return;
            }
            invokeData(data, list);
        }


    }

    private void invokeData(PostSigleData data, CopyOnWriteArrayList<Subscription> list) {
        for (Subscription subscription : list) {
            Class<?> clzz = data.data.getClass();
            if (clzz.isAssignableFrom(subscription.subscriberMethod.eventType)) {
                try {
                    if (subscription.subscriberMethod.mainThread) {//主线程
                        Message message = new Message();
                        SubInfo subInfo = new SubInfo(data.data, subscription);
                        message.obj = subInfo;
                        mHandler.sendMessage(message);
                    } else {
                        subscription.subscriberMethod.method.invoke(subscription.subscriber, data.data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 解订阅
     *
     * @param subscriber
     */
    public void unRegister(Object subscriber) {
        CopyOnWriteArrayList<Subscription> list = mSubscription.get(subscriber.getClass());
        if (list != null && !list.isEmpty()) {
            for (Subscription subscription : list) {
                if (subscription.subscriber == subscriber) {
                    subscription.subscriber = null;
                    subscription.subscriberMethod.method = null;
                    subscription.subscriberMethod.eventType = null;
                    subscription.subscriberMethod = null;
                    list.remove(subscription);
                }
            }
            if (list.isEmpty()) {
                mMethodFinder.removeClazzInfo(subscriber.getClass());
                mSubscription.remove(subscriber.getClass());
            }
        }
    }
}
