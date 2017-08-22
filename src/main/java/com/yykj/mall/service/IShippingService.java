package com.yykj.mall.service;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Shipping;

/**
 * Created by Lee on 2017/8/22.
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> detail(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
