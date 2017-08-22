package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.vo.CartVo;

/**
 * Created by Lee on 2017/8/21.
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProducts(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> checkOrUnCheck(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getProductCount(Integer userId);
}
