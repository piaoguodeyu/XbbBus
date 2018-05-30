package com.xbb;

import android.os.Bundle;
import android.view.View;

import com.xbbbus.XbbBus;
import com.xbbbus.XbbMainThreadSubscriber;
import com.xbbbus.XbbSubscriber;

public class SecondActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void mainAct(View view) {
        XbbBus.getDefaut().post("tomain", MainActivity.class);
    }

    public void currentAct(View view) {
        XbbBus.getDefaut().post("toSecondActivity", SecondActivity.class);

    }

    public void toAll(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                XbbBus.getDefaut().post("childThread");
            }
        }).start();

    }


    /**
     * 该方法是在主线程执行的
     *
     * @param string
     */
    @XbbMainThreadSubscriber
    void content(String string) {
        toast("SecondActivity.contentCurrentThread " + string);
    }

    /**
     * 当前线程执行
     *
     * @param string
     */
    @XbbSubscriber
    void contentCurrentThread(String string) {
        toast("SecondActivity.contentCurrentThread " + string);
    }
}
