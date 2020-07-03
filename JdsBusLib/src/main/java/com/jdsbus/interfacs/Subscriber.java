package com.jdsbus.interfacs;

/**
 * @author zhangxiaowei 2020/6/21
 */
public interface Subscriber<T> {

    Subscriber<T> observeOn(SchedulerType schedulerType);


    void subscriber(Observer<T> subscriber);
}
