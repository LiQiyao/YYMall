package com.yykj.mall.dao;

import com.yykj.mall.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectByUserId(Integer userId);

    int selectUnCheckedByUserId(Integer userId);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    int deleteByUserIdAndProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int updateCheckedOrUnchecked(@Param("userId") Integer userId,@Param("productId") Integer productId, @Param("checked") Integer checked);

    int selectProductCount(Integer userId);

    List<Cart> selectCheckedByUserId(Integer userId);
}