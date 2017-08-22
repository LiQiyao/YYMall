package com.yykj.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.ShippingMapper;
import com.yykj.mall.pojo.Shipping;
import com.yykj.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lee on 2017/8/22.
 */
@Service
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int res = shippingMapper.insertSelective(shipping);
        if (res > 0){
        }
    }

    public ServerResponse delete(Integer userId, Integer shippingId){

    }

    public ServerResponse update(Integer userId, Shipping shipping){

    }

    public ServerResponse<Shipping> detail(Integer userId, Integer shippingId){

    }

    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
