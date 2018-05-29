package com.xbbbus;

/**
 * Created by zhangxiaowei on 18/4/18.
 * 用于主函数订阅
 */

class SubInfo {
    final public Object data;
    final public Subscription subscription;

    public SubInfo(Object data, Subscription subscription) {
        this.data = data;
        this.subscription = subscription;
    }
}
