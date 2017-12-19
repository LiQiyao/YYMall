package com.yykj.mall.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Lee on 2017/8/20.
 */
public class CartDTO {

    private List<CartProductDTO> cartProductDTOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductDTO> getCartProductDTOList() {
        return cartProductDTOList;
    }

    public void setCartProductDTOList(List<CartProductDTO> cartProductDTOList) {
        this.cartProductDTOList = cartProductDTOList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
