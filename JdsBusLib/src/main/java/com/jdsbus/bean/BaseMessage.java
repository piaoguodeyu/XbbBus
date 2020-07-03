package com.jdsbus.bean;

/**
 * Created by zhangxiaowei on 17/4/25.
 */

class BaseMessage {
    /**
     * 每个事件对应的id
     */
    public int event;
    /**
     * 事件所携带的信息
     */
    public Object data;

    public BaseMessage() {
        super();
    }

    public BaseMessage(int event, Object data) {
        super();
        this.event = event;
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseMessage [event=" + event + ", data=" + data + "]";
    }

}
