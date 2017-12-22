package com.yykj.mall.dao;

import com.yykj.mall.entity.Collection;
import com.yykj.mall.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);

    int deleteByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Product> listCollectedProductsByUserId(Integer userId);

    Integer selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}