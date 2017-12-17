package com.yykj.mall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Lee on 2017/8/29.
 */
public class OrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;

    private Integer postage;

    private Integer status;


    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    //订单的明细
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;
    private Integer shippingId;
    private String receiverName;

    private ShippingVo shippingVo;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public ShippingVo getShippingVo() {
        return shippingVo;
    }

    public void setShippingVo(ShippingVo shippingVo) {
        this.shippingVo = shippingVo;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public void setPaymentTypeDesc(String paymentTypeDesc) {
        this.paymentTypeDesc = paymentTypeDesc;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public String getPaymentTypeDesc() {
        return paymentTypeDesc;
    }

    public Integer getPostage() {
        return postage;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String getCreateTime() {
        return createTime;
    }
}
