package com.jdsbus;

/**
 * Created by zhangxiaowei on 17/4/26.
 */

class PostSigleData {
    final public Object data;
    final Class<?> sigleClazz;

    public PostSigleData(Object data, Class<?> sigleClazz) {
        this.data = data;
        this.sigleClazz = sigleClazz;
    }
}
