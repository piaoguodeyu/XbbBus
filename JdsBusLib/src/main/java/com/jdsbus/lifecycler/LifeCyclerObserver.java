package com.jdsbus.lifecycler;

import android.arch.lifecycle.LifecycleOwner;

import com.jdsbus.baseobservable.BaseObservable;

/**
 * @author zhangxiaowei 2020/6/21
 */
public class LifeCyclerObserver extends BaseObservable {
    public LifeCyclerObserver(JdsBusLifeCycler lifeCycler, LifecycleOwner lifecycleOwner) {
        super(lifeCycler, lifecycleOwner);
    }
}
