package com.yykj.mall.service;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dto.OrderDTO;

import java.util.Map;

/**
 * Created by Lee on 2017/8/26.
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse<Boolean> queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse create(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId, Long orderNo);

    ServerResponse getCartCheckedProduct(Integer userId);

    ServerResponse<OrderDTO> getDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    ServerResponse<OrderDTO> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);
}
