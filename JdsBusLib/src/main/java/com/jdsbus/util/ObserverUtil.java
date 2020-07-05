package com.jdsbus.util;

import android.os.Looper;
import android.os.Message;

import com.jdsbus.JdsBus;
import com.jdsbus.baseobservable.BaseObservable;
import com.jdsbus.interfacs.PostMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhangxiaowei 2020/7/2
 */
public class ObserverUtil {
    SubscriberMethodFinder mMethodFinder;
    ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Subscription>> mSubscription;
    HandlerUtil mHandler;
    /**
     * 缓存订阅
     */
    ConcurrentHashMap<Class, Map<BaseObservable, BaseObservable>> subscriberList;
    /**
     * 缓存订阅
     */
    ConcurrentHashMap<Class, PostMessage> subscriberList;
    private static volatile ObserverUtil mObserverUtil;


    public static ObserverUtil getDefaut() {
        if (mObserverUtil == null) {
            synchronized (JdsBus.class) {
                if (mObserverUtil == null) {
                    mObserverUtil = new ObserverUtil();
                }
            }
        }
        return mObserverUtil;
    }

    private ObserverUtil() {
        mMethodFinder = new SubscriberMethodFinder();
        mSubscription = new ConcurrentHashMap<>();
        mHandler = new HandlerUtil(Looper.getMainLooper());
        subscriberList=new ConcurrentHashMap<>();
    }

    void registerObserver(Class clasz, Object object) {

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
                for (SubscriberMethod method : subscriberMethods) {
                    Subscription subscription = new Subscription(subscriber, method, clazz);
                    subscriptions.add(subscription);
                }
            }
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


    public void post(Object data, Class<?> clazz) {
        try {
            PostSigleData sigleData = new PostSigleData(data, clazz);
            postToSubscripber(sigleData);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (subscription.getSubscriber() == subscriber) {
                    removeSub(list, subscription);
                }
            }
        }
    }

}
