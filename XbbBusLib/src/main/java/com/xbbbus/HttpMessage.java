package com.xbbbus;

/**
 * Created by zhangxiaowei on 17/4/25.
 */

public class HttpMessage extends BaseMessage {
    /**
     * 网络请求成功
     */
    public boolean success;
    /**
     * 对返回结果的描述
     */
    public String description;
    public int code;
   public Object mOther;

    public HttpMessage(int event, boolean success, String description, int code, Object data) {
        super(event, data);
        this.success = success;
        this.description = description;
        this.code = code;
    }

    public HttpMessage(int event, boolean success, Object data) {
        super(event, data);
        this.success = success;
    }

    public HttpMessage(int event, boolean success) {
        super(event, null);
        this.success = success;
    }

    public HttpMessage(int event, boolean success, String description, int code) {
        super(event, null);
        this.success = success;
        this.description = description;
        this.code = code;
    }

    @Override
    public String toString() {
        return "HttpMessage{" +
                "success=" + success +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
