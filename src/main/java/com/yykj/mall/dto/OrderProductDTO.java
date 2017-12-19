package com.yykj.mall.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Lee on 2017/8/29.
 */
public class OrderProductDTO {
    private List<OrderItemDTO> orderItemDTOList;
    private BigDecimal productTotalPrice;
    private String imageHost;

    public List<OrderItemDTO> getOrderItemDTOList() {
        return orderItemDTOList;
    }

    public void setOrderItemDTOList(List<OrderItemDTO> orderItemDTOList) {
        this.orderItemDTOList = orderItemDTOList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
