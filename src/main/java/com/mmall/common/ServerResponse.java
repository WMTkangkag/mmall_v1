package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 高可复用serverresponser类
 * Created by txk on 2018/5/28.
 */
@JsonSerialize(include =JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候，如果是null对象，key也会消失
public class ServerResponse<T> implements Serializable{

    private  int status;
    private  String massage;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String massage, T data) {
        this.status = status;
        this.massage = massage;
        this.data = data;
    }

    private ServerResponse(int status, T data) {

        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String massage) {

        this.status = status;
        this.massage = massage;
    }

    @JsonIgnore
    //使之不在json序列化结果中
    public  boolean isSuccess(){
        return  this.status==ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMassage() {
        return massage;
    }

    public T getData() {
        return data;
    }

    public static <T>  ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMassage(String msg){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public  static <T> ServerResponse<T> createBySuccess(T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public  static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg, data);
    }

    public static <T>  ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByErrorMassage(String msg){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T>  ServerResponse<T> createByError(int error,String msg){
        return new ServerResponse<T>(error,msg);
    }


}
