package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dto.CartDTO;

/**
 * Created by Lee on 2017/8/21.
 */
public interface ICartService {

    ServerResponse<CartDTO> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartDTO> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartDTO> deleteProducts(Integer userId, String productIds);

    ServerResponse<CartDTO> list(Integer userId);

    ServerResponse<CartDTO> checkOrUnCheck(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getProductCount(Integer userId);
}
