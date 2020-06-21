package com.jdsbus.interfacs;

/**
 * @author zhangxiaowei 2020/6/21
 */
public interface Subscriber<D> {
    void subscriber(Observer<D> subscriber);

    Subscriber<D> observeOn(SchedulerType schedulerType);
}
