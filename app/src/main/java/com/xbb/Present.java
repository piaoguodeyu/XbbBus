package com.xbb;

import android.util.Log;

import com.jdsbus.JdsSubscriber;

/**
 * @author zhangxiaowei 2020-02-29
 */
public class Present {
    public Present() {
    }

    @JdsSubscriber
    void say(String str) {
//        Log.e("PresentPresent", "str= "+str);
    }
}
