package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Product;

/**
 * Created by Lee on 2017/8/17.
 */
public interface IProductService {

    ServerResponse saveOrUpdate(Product product);

    ServerResponse setStatus(Integer productId, Integer status);


}
