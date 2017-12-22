package com.yykj.mall.service.impl;

import com.yykj.mall.common.GenericBuilder;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.CollectionMapper;
import com.yykj.mall.entity.Collection;
import com.yykj.mall.entity.Product;
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
    public ServerResponse<Boolean> collect(Integer userId, Integer productId) {
        if (userId == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int resultCount = collectionMapper.insertSelective(GenericBuilder.of(Collection::new)
                .with(Collection::setUserId, userId)
                .with(Collection::setProductId, productId)
                .with(Collection::setCreateTime, new Date())
                .build());
        if (resultCount > 0){
            return ServerResponse.createBySuccess("收藏成功", true);
        } else {
            return ServerResponse.createByErrorMessage("收藏失败！");
        }
    }

    @Override
    public ServerResponse<Boolean> cancelCollect(Integer userId, Integer productId) {
        if (userId == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int resultCount = collectionMapper.deleteByUserIdAndProductId(userId, productId);
        if (resultCount > 0){
            return ServerResponse.createBySuccess("取消收藏成功！", false);
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

    @Override
    public ServerResponse<Boolean> getCollectionStatus(Integer userId, Integer productId) {
        if (userId != null && productId != null){
            Integer resultCount = collectionMapper.selectByUserIdAndProductId(userId, productId);
            if (resultCount != null && resultCount > 0){
                return ServerResponse.createBySuccess(true);
            } else {
                return ServerResponse.createBySuccess(false);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }
}
