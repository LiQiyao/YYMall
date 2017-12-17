package com.yykj.mall.dao;

import com.yykj.mall.pojo.FlashSale;

public interface FlashSaleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FlashSale record);

    int insertSelective(FlashSale record);

    FlashSale selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FlashSale record);

    int updateByPrimaryKey(FlashSale record);
}