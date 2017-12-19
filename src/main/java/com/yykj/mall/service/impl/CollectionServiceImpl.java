package com.yykj.mall.service.impl;

import com.yykj.mall.common.GenericBuilder;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.CollectionMapper;
import com.yykj.mall.pojo.Collection;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @date 2017/12/1
 */
@Service
public class CollectionServiceImpl implements ICollectionService{

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public ServerResponse collect(Integer userId, Integer productId) {
        if (userId == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int resultCount = collectionMapper.insertSelective(GenericBuilder.of(Collection::new)
                .with(Collection::setUserId, userId)
                .with(Collection::setProductId, productId)
                .with(Collection::setCreateTime, new Date())
                .build());
        if (resultCount > 0){
            return ServerResponse.createBySuccessMessage("收藏成功！");
        } else {
            return ServerResponse.createByErrorMessage("收藏失败！");
        }
    }

    @Override
    public ServerResponse cancelCollect(Integer userId, Integer productId) {
        if (userId == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int resultCount = collectionMapper.deleteByUserIdAndProductId(userId, productId);
        if (resultCount > 0){
            return ServerResponse.createBySuccessMessage("取消收藏成功！");
        } else {
            return ServerResponse.createByErrorMessage("取消收藏失败！");
        }

    }

    @Override
    public ServerResponse<List<Product>> listCollectedProduct(Integer userId) {
        if (userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.createBySuccess(collectionMapper.listCollectedProductsByUserId(userId));
    }
}
