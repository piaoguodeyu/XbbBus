package com.xbbbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanågxiaowei on 17/4/26.
 */

class SubscriberMethodFinder {
    private static final Map<String, List<SubscriberMethod>> methodCache = new HashMap<>();

    public List<SubscriberMethod> getMitakeMethod(Class clazz) {

        List<SubscriberMethod> methodList;
        synchronized (clazz) {
            methodList = methodCache.get(clazz.getName());
        }
        if (methodList != null) {
            return methodList;
        }
        methodList = new ArrayList<>();
        List<Method> methodslist = new ArrayList<>();
        Class clazzdata = clazz;
        while (clazzdata != null) {
            Method[] methods = clazzdata.getDeclaredMethods();
            methodslist.addAll(Arrays.asList(methods));
            clazzdata = clazzdata.getSuperclass();
        }
        for (Method method : methodslist) {
            method.setAccessible(true);
            Class[] methPt = method.getParameterTypes();
            if (methPt != null && methPt.length == 1) {
                if (method.isAnnotationPresent(XbbSubscriber.class)) {
                    SubscriberMethod subscriberMethod = new SubscriberMethod(method, methPt[0]);
                    subscriberMethod.mainThread = false;
                    methodList.add(subscriberMethod);
                } else if (method.isAnnotationPresent(XbbMainThreadSubscriber.class)) {
                    SubscriberMethod subscriberMethod = new SubscriberMethod(method, methPt[0]);
                    subscriberMethod.mainThread = true;
                    methodList.add(subscriberMethod);
                }
            }

        }

        if (methodList.isEmpty()) {
            throw new XbbException("Subscriber " + clazz + " 未找到订阅方法 ");
        } else {
            synchronized (clazz) {
                methodCache.put(clazz.getName(), methodList);
            }
            return methodList;
        }
    }

    public void removeClazzInfo(Class clazz) {
        synchronized (clazz) {
            methodCache.remove(clazz.getName());
        }
    }
}
