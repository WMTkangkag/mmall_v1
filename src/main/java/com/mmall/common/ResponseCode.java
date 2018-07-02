package com.mmall.common;

/**
 * Created by txk on 2018/5/28.
 */
public enum  ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    illegal_argument(2,"ILLEGAL_ARGUMENT");

    private  final  int code;
    private  final  String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
