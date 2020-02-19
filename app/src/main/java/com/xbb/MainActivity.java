package com.xbb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdsbus.JdsMainThreadSubscriber;
import com.jdsbus.JdsSubscriber;

public class MainActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSecond(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    /**
     * 该方法是在主线程执行的
     *
     * @param string
     */
    @JdsMainThreadSubscriber
    void content(String string) {
        toast("MainActivity.MainThread " + string);
    }

    /**
     * 当前线程执行
     *
     * @param string
     */
    @JdsSubscriber
    void contentCurrentThread(String string) {
        toast("MainActivity.11111 " + string);
    }
}
