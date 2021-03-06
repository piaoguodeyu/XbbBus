package com.jdsbus.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jdsbus.JdsBus;
import com.jdsbus.util.SubInfo;

/**
 * Created by zhangxiaowei on 18/4/18.
 * 将订阅信息推送到主线程
 */

class HandlerUtil extends Handler {
    public HandlerUtil(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            SubInfo info = (SubInfo) msg.obj;
            if (info.subscription.getSubscriber() == null) {
                ObserverUtil.getDefaut().removeSub(info.subscription);
            } else {
                info.subscription.subscriberMethod.method.invoke(info.subscription.getSubscriber(), info.data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
