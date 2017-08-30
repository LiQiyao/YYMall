package com.yykj.mall.util;

import java.math.BigDecimal;

/**
 * Created by Lee on 2017/8/20.
 */
public class BigDecimalUtil {

    private BigDecimalUtil(){

    }

    public static BigDecimal add(double v1, double v2){
        BigDecimal b0 = new BigDecimal(Double.toString(v1));
        BigDecimal b1 = new BigDecimal(Double.toString(v2));
        return b0.add(b1);
    }

    public static BigDecimal subtract(double v1, double v2){
        BigDecimal b0 = new BigDecimal(Double.toString(v1));
        BigDecimal b1 = new BigDecimal(Double.toString(v2));
        return b0.subtract(b1);
    }

    public static BigDecimal multiply(double v1, double v2){
        BigDecimal b0 = new BigDecimal(Double.toString(v1));
        BigDecimal b1 = new BigDecimal(Double.toString(v2));
        return b0.multiply(b1);
    }

    public static BigDecimal divide(double v1, double v2){
        BigDecimal b0 = new BigDecimal(Double.toString(v1));
        BigDecimal b1 = new BigDecimal(Double.toString(v2));
        return b0.divide(b1, 2, BigDecimal.ROUND_HALF_UP);
    }
}
