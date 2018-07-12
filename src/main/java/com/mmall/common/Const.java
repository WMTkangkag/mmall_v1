package com.mmall.common;

/**
 * Created by txk on 2018/5/28.
 */
public class  Const {

   public interface RedisCacheExtime{
      int REDIS_SESSION_EXTIME=60*30;
   }

   public static final String CURRENT_USER="currentUser";

   public  static final String USERNAME="username";

   public  static final String EMAIL="email";

   public interface OrderBy{
      String PRICE_DESC="desc";
      String PRICE_ASC="asc";
   }

   public interface Cart{

      int CHECKED=1;
      int UN_CHECKED=0;

      String LIMIT_QUANTITY_SUCCESS="LIMIT_QUANTITY_SUCCESS";
      String LIMIT_QUANTITY_FAIL="LIMIT_QUANTITY_FAIL";

   }


   public  interface  Role{
      int ROLE_CUSTOMER=0;//普通用户
      int ROLE_ADMIN=1;//管理员
   }

   public enum ProductStatus{

      ONSALE(1,"在线"),
      SALEOUT(2,"下架"),
      DELETEPRODUCT(3,"删除");


      private Integer status;

      private String desc;

      ProductStatus(Integer status, String desc) {
         this.status = status;
         this.desc = desc;
      }

      public Integer getStatus() {
         return status;
      }

      public String getDesc() {
         return desc;
      }


   }

   public enum OrderStatusEnum{
      CANCELED(0,"已取消"),
      NO_PAY(10,"未支付"),
      PAID(20,"已付款"),
      SHIPPED(40,"已发货"),
      ORDER_SUCCESS(50,"订单完成"),
      ORDER_CLOSED(60,"订单关闭");



      OrderStatusEnum(int code,String value){
         this.code=code;
         this.value=value;
      }
      private int code;
      private String value;

      public int getCode() {
         return code;
      }

      public void setCode(int code) {
         this.code = code;
      }

      public String getValue() {
         return value;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public static OrderStatusEnum codeOf(int code){
         for(OrderStatusEnum OrderStatusEnum:values()){
            if(code==OrderStatusEnum.getCode()){
               return OrderStatusEnum;
            }
         }
         throw new RuntimeException("没有找到对应的枚举");
      }

   }

   public interface AlipayCallback{
      String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
      String TRADE_SATUS_TRADE_SUCCESS="TRADE_SUCCESS";
      String RESPONSE_SUCCEESS="SUCCESS";
      String RESPONSE_FAILED="FAILED";
   }

   public enum PayPlatformEnum{
      ALIPAY(1,"支付宝");


      private int code;
      private String value;
      PayPlatformEnum(int code,String value){
         this.code=code;
         this.value=value;
      }

      public int getCode() {
         return code;
      }

      public void setCode(int code) {
         this.code = code;
      }

      public String getValue() {
         return value;
      }

      public void setValue(String value) {
         this.value = value;
      }
   }

   public enum PaymentTypeEnum{
      ONLINE_PAY(1,"在线支付");

      private int code;
      private String value;
      PaymentTypeEnum(int code,String value){
         this.code=code;
         this.value=value;
      }

      public int getCode() {
         return code;
      }

      public void setCode(int code) {
         this.code = code;
      }

      public String getValue() {
         return value;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public static PaymentTypeEnum codeOf(int code){
         for(PaymentTypeEnum paymentTypeEnum:values()){
            if(code==paymentTypeEnum.getCode()){
               return paymentTypeEnum;
            }
         }
         throw new RuntimeException("没有找到对应的枚举");
      }

   }
}
