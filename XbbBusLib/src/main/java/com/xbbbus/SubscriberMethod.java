package com.xbbbus;

import java.lang.reflect.Method;

/**
 * Created by zhangxiaowei on 17/4/28.
 */

final class SubscriberMethod {
    /**
     * 订阅者方法
     */
     public Method method;
    /**
     * 订阅者方法参数类型
     */
    public Class<?> eventType;
    /**
     * 该方法是否在主线程执行
     */
    public boolean mainThread;

    SubscriberMethod(Method method, Class<?> eventType) {
        this.method = method;
        this.eventType = eventType;
    }


    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
