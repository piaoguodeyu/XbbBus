package com.jdsbus.interfacs;

import java.lang.reflect.Field;

/**
 * @author zhangxiaowei 2020/6/21
 */
public interface Observer<T> {

    void handMessage(T mesage);

}
