package com.yykj.mall.service.impl;

import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.ProductMapper;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lee on 2017/8/17.
 */
@Service
public class IProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdate(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                product.setMainImage(subImageArray[0]);
            }
            if (product.getId() == null) {
                int res = productMapper.insertSelective(product);
                if (res > 0){
                    return ServerResponse.createBySuccessMessage("新增产品成功！");
                }
                return ServerResponse.createByErrorMessage("新增产品失败！");
            } else {
                int res = productMapper.updateByPrimaryKey(product);
                if (res > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功！");
                }
                return ServerResponse.createByErrorMessage("更新产品失败！");
            }
        }
        return ServerResponse.createByErrorMessage("产品参数不正确！");
    }

    @Override
    public ServerResponse setStatus(Integer productId, Integer status) {
        if (productId != null && status != null){
            Product product = new Product();
            product.setId(productId);
            product.setStatus(status);
            int res = productMapper.updateByPrimaryKeySelective(product);
            if (res > 0){
                return ServerResponse.createBySuccessMessage("修改产品状态成功！");
            }
            return ServerResponse.createByErrorMessage("修改产品状态失败！");
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

}
