package com.yykj.mall.dao;

import com.yykj.mall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    List<Shipping> selectByUserId(Integer userId);

    int deleteByShippingIdAndUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    int updateByRecord(Shipping shipping);

    Shipping selectByShippingIdAndUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);
}