package com.mmall.util;

import java.math.BigDecimal;

/**
 * Created by txk on 2018/6/8.
 */
//用于解决商业运算中浮点型丢失精度的问题
public class BigDecimalUtil {


    /**
     * 相加
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(Double a,Double b){
        BigDecimal a1=new BigDecimal(Double.toString(a));
        BigDecimal b1=new BigDecimal(Double.toString(b));
        return a1.add(b1);
    }

    /**
     * 相减
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal sub(Double a,Double b){
        BigDecimal a1=new BigDecimal(Double.toString(a));
        BigDecimal b1=new BigDecimal(Double.toString(b));
        return a1.subtract(b1);
    }

    /**
     * 相乘
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal mul(Double a,Double b){
        BigDecimal a1=new BigDecimal(Double.toString(a));
        BigDecimal b1=new BigDecimal(Double.toString(b));
        return a1.multiply(b1);
    }

    /**
     * 相除（四舍五入）
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal div(Double a,Double b){
        BigDecimal a1=new BigDecimal(Double.toString(a));
        BigDecimal b1=new BigDecimal(Double.toString(b));
        return a1.divide(b1,2,BigDecimal.ROUND_HALF_UP);
    }




}
