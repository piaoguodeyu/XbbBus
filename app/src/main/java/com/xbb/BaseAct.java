package com.xbb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jdsbus.JdsBus;

/**
 * Created by zhangxiaowei on 18/5/29.
 */

public class BaseAct extends Activity {
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JdsBus.getDefaut().register(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        content = findViewById(R.id.content);
    }

    @Override
    protected void onDestroy() {
        JdsBus.getDefaut().unRegister(this);
        super.onDestroy();
    }

    void toast(String string) {
        content.setText(string);
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
