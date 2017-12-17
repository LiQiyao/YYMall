package com.yykj.mall.pojo;

import java.util.Date;

public class Collection {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Date createTime;

    public Collection(Integer id, Integer userId, Integer productId, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.createTime = createTime;
    }

    public Collection() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}