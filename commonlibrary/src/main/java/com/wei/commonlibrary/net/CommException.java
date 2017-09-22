package com.wei.commonlibrary.net;

/**
 * Created by ${wei} on 2017/3/21.
 */

public class CommException extends Exception {

    private String message;
    private int code;
    private int type;

    public CommException() {
    }

    public CommException(String message) {
        super(message);
        this.message = message;
    }

    public CommException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CommException{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", type=" + type +
                '}';
    }
}
