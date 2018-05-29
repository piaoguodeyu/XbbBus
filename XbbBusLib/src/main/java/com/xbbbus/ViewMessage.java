package com.xbbbus;

/**
 * Created by zhangxiaowei on 17/5/4.
 */

public class ViewMessage extends BaseMessage {
    public Object other;

    public ViewMessage() {
    }

    public ViewMessage(int event, Object data) {
        super(event, data);
    }
}
