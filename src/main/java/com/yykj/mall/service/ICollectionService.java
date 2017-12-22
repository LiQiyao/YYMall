package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.entity.Product;

import java.util.List;

/**
 * @author Lee
 * @date 2017/12/1
 */
public interface ICollectionService {

    ServerResponse<Boolean> collect(Integer userId, Integer productId);

    ServerResponse<Boolean> cancelCollect(Integer userId, Integer productId);

    ServerResponse<List<Product>> listCollectedProduct(Integer userId);

    ServerResponse<Boolean> getCollectionStatus(Integer userId, Integer productId);
}
