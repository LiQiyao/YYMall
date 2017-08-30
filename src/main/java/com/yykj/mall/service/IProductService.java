package com.yykj.mall.service;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.vo.ProductDetailVo;

/**
 * Created by Lee on 2017/8/17.
 */
public interface IProductService {

    ServerResponse saveOrUpdate(Product product);

    ServerResponse setStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordAndCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
