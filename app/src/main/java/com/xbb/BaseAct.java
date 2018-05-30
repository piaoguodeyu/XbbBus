package com.xbb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.xbbbus.XbbBus;

/**
 * Created by zhangxiaowei on 18/5/29.
 */

public class BaseAct extends Activity {
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XbbBus.getDefaut().register(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        content = findViewById(R.id.content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XbbBus.getDefaut().unRegister(this);
    }

    void toast(String string) {
        content.setText(string);
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
