package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Product;

import java.util.List;

/**
 * @author Lee
 * @date 2017/12/1
 */
public interface ICollectionService {

    ServerResponse collect(Integer userId, Integer productId);

    ServerResponse cancelCollect(Integer userId, Integer productId);

    ServerResponse<List<Product>> listCollectedProduct(Integer userId);
}
