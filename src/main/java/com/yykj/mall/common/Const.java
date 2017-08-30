package com.yykj.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Lee on 2017/8/13.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");//contains方法 O(1)复杂度
    }

    public enum ProductStatus{
        ON_SALE(1, "正在销售");

        private int code;
        private String desc;

        ProductStatus(int code, String desc) {
            this.desc = desc;
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public int getCode() {
            return code;
        }
    }

    public interface Cart{
        int CHECKED = 1;//选中
        int UN_CHECKED = 0;//未选中
        String LIMIT_QUANTITY_FAIL = "LIMIT_QUANTITY_FAIL";//订货量超过库存
        String LIMIT_QUANTITY_SUCCESS = "LIMIT_QUANTITY_SUCCESS";//未超过库存
    }

    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        UNPAID(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");;

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()){
                if (orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("未找到相应的枚举类型");
        }

        public String getValue() {
            return value;
        }
    }

    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum{
        ALIPAY(1, "支付宝");

        private int code;
        private String value;

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum{
        ONLINE_PAY(1, "在线支付");

        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static PaymentTypeEnum codeOf(int code){
            for (PaymentTypeEnum paymentTypeEnum : values()){
                if (paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("未找到该枚举类型");
        }
    }
}
