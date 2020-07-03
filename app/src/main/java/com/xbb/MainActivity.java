package com.xbb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jdsbus.JdsBus;
import com.jdsbus.interfacs.JdsMainThreadSubscriber;
import com.jdsbus.interfacs.JdsSubscriber;
import com.jdsbus.interfacs.Observer;
import com.jdsbus.interfacs.SchedulerType;
import com.xbb.bean.Student;

public class MainActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        JdsBus.getDefaut().
//                bindOnDestroyLifeCycler(this)
//                .register(Student.class)
//                .observeOn(SchedulerType.main)
//                .subscriber(new Observer<Student>() {
//                    @Override
//                    public void handMessage(Student mesage) {
//
//                    }
//                });

        JdsBus.bindOnDestroyLifeCycler(this)
                .register(Student.class)
                .observeOn(SchedulerType.main)
                .subscriber(new Observer<Student>() {
                    @Override
                    public void handMessage(Student mesage) {

                    }
                });
    }

    public void openSecond(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void fasongdier(View view) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            JdsBus.getDefaut().post("来了-----");
        }
        time = System.currentTimeMillis() - time;
        Log.e("fasongdierfasongdier", "time= " + time);
    }

    /**
     * 该方法是在主线程执行的
     *
     * @param string
     */
    @JdsMainThreadSubscriber
    void content(String string) {
//        toast("MainActivity.MainThread " + string);
    }

    /**
     * 当前线程执行
     *
     * @param string
     */
    @JdsSubscriber
    void contentCurrentThread(String string) {
//        toast("MainActivity.11111 " + string);
    }
}
