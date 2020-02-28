package com.jdsbus;

import android.os.Looper;
import android.os.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhangxiaowei on 17/4/25.
 */

public class JdsBus {

    private static volatile JdsBus mMitakeBus;
    SubscriberMethodFinder mMethodFinder;
    ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Subscription>> mSubscription;
    HandlerUtil mHandler;

    public static JdsBus getDefaut() {
        if (mMitakeBus == null) {
            synchronized (JdsBus.class) {
                if (mMitakeBus == null) {
                    mMitakeBus = new JdsBus();
                }
            }
        }
        return mMitakeBus;
    }

    private JdsBus() {
        mMethodFinder = new SubscriberMethodFinder();
        mSubscription = new ConcurrentHashMap<>();
        mHandler = new HandlerUtil(Looper.getMainLooper());
    }

    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        synchronized (clazz) {
            List<SubscriberMethod> subscriberMethods = mMethodFinder.getMitakeMethod(clazz);
            if (!subscriberMethods.isEmpty()) {
                CopyOnWriteArrayList<Subscription> subscriptions = mSubscription.get(clazz);
                if (subscriptions == null) {
                    subscriptions = new CopyOnWriteArrayList<>();
                    mSubscription.put(clazz, subscriptions);
                }
//                Log.e("subscriptions ", "" + subscriptions.toString());
                for (SubscriberMethod method : subscriberMethods) {
                    Subscription subscription = new Subscription(subscriber, method, clazz);
                    subscriptions.add(subscription);
                }
            }
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
                    if (subscription.getSubscriber() == null) {
                        removeSub(list, subscription);
                        continue;
                    }
                    if (subscription.subscriberMethod.mainThread) {//主线程
                        Message message = new Message();
                        SubInfo subInfo = new SubInfo(data.data, subscription);
                        message.obj = subInfo;
                        mHandler.sendMessage(message);
                    } else {
                        subscription.subscriberMethod.method.invoke(subscription.getSubscriber(), data.data);
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
//                try {
//                    Log.e("unRegisterunRegister ", "subscriber= " + subscriber.toString()
//                            + " subscription.subscriber= " + subscription.getSubscriber().toString() + " bool= "
//                            + (subscription.getSubscriber() == subscriber) + " size= " + list.size());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                if (subscription.getSubscriber() == subscriber) {
                    removeSub(list, subscription);
                }
            }
//            Log.i("unRegisterunRegister ", "subscriber= " + list.isEmpty() + " mSubscription= "
//                    + mSubscription.toString() + " size= " + mSubscription.size());
//            if (list.isEmpty()) {
//                mMethodFinder.removeClazzInfo(subscriber.getClass());
//                mSubscription.remove(subscriber.getClass());
//            }
        }
    }

    void removeSub(Subscription subscription) {
        CopyOnWriteArrayList<Subscription> list = mSubscription.get(subscription.clazz);
        removeSub(list, subscription);
    }

    private void removeSub(CopyOnWriteArrayList<Subscription> list, Subscription subscription) {
        list.remove(subscription);
        if (list.isEmpty()) {
            subscription.clearSubscriber();
            subscription.subscriberMethod.method = null;
            subscription.subscriberMethod.eventType = null;
            subscription.subscriberMethod = null;
        }
    }
}
