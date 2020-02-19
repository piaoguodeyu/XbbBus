package com.xbb;

import android.os.Bundle;
import android.view.View;

import com.jdsbus.JdsBus;
import com.jdsbus.JdsMainThreadSubscriber;
import com.jdsbus.JdsSubscriber;

public class SecondActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void mainAct(View view) {
        JdsBus.getDefaut().post("tomain", MainActivity.class);
    }

    public void currentAct(View view) {
        JdsBus.getDefaut().post("toSecondActivity", SecondActivity.class);

    }

    public void toAll(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JdsBus.getDefaut().post("childThread");
            }
        }).start();

    }


    /**
     * 该方法是在主线程执行的
     *
     * @param string
     */
    @JdsMainThreadSubscriber
    void content(String string) {
        toast("SecondActivity.contentCurrentThread " + string);
    }

    /**
     * 当前线程执行
     *
     * @param string
     */
    @JdsSubscriber
    void contentCurrentThread(String string) {
        toast("SecondActivity.contentCurrentThread " + string);
    }
}
