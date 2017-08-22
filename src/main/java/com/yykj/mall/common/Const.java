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
}
