package com.jdsbus.interfacs;

/**
 * @author zhangxiaowei 2020/6/21
 */
public interface Register {
    <D> Subscriber<D> register(Class<D> subscriber);

    void register(Object subscriber);
}
