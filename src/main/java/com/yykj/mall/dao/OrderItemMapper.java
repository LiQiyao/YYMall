package com.yykj.mall.dao;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> selectByOrderNoAndUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    int batchInsert(List<OrderItem> orderItemList);

    List<OrderItem> selectByOrderNo(Long orderNo);


}