package com.xbb;

import com.jdsbus.interfacs.JdsSubscriber;

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
