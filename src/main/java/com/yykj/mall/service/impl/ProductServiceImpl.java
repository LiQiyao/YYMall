package com.yykj.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.yykj.mall.common.Const;
import com.yykj.mall.common.ResponseCode;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.CategoryMapper;
import com.yykj.mall.dao.ProductMapper;
import com.yykj.mall.pojo.Category;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.ICategoryService;
import com.yykj.mall.service.IProductService;
import com.yykj.mall.util.DateTimeUtil;
import com.yykj.mall.util.PropertiesUtil;
import com.yykj.mall.dto.ProductDetailDTO;
import com.yykj.mall.dto.ProductListItemDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lee on 2017/8/17.
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

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
                return ServerResponse.createBySuccessMessage("修改商品状态成功！");
            }
            return ServerResponse.createByErrorMessage("修改商品状态失败！");
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    @Override
    public ServerResponse<ProductDetailDTO> manageProductDetail(Integer productId){
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createBySuccessMessage("商品不存在！");
        }
        return ServerResponse.createBySuccess(assembleProductDetailDTO(product));
    }

    private ProductDetailDTO assembleProductDetailDTO(Product product){
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        BeanUtils.copyProperties(product, productDetailDTO);
        productDetailDTO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productDetailDTO.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        productDetailDTO.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));

        Category category = categoryMapper.selectByPrimaryKey(product.getId());
        if (category == null){
            productDetailDTO.setParentCategoryId(0);//默认设为根节点
        } else {
            productDetailDTO.setParentCategoryId(category.getParentId());
        }
        return productDetailDTO;
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectAll();
        List<ProductListItemDTO> productListVoList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListItemDTO productListItemDTO = assembleProductListItemDTO(productItem);
            productListVoList.add(productListItemDTO);
        }
        PageInfo pageInfo = new PageInfo(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListItemDTO assembleProductListItemDTO(Product product){
        ProductListItemDTO productListItemDTO = new ProductListItemDTO();
        BeanUtils.copyProperties(product, productListItemDTO);
        productListItemDTO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.yykj.com/"));
        return productListItemDTO;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByNameAndProductId("%" + productName + "%", productId);
        List<ProductListItemDTO> productListVoList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListItemDTO productListItemDTO = assembleProductListItemDTO(productItem);
            productListVoList.add(productListItemDTO);
        }
        PageInfo pageInfo = new PageInfo(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<ProductDetailDTO> getProductDetail(Integer productId) {
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus() != Const.ProductStatus.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架或删除！");
        }
        ProductDetailDTO productDetailDTO = assembleProductDetailDTO(product);
        return ServerResponse.createBySuccess(productDetailDTO);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordAndCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null){//两个参数都不合法的情况下
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<ProductListItemDTO> productListVoList = Lists.newArrayList();
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){//如果分类ID不为空
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){//找不到该分类，且没有关键词的情况下，返回空结果集
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getDeepChildrenCategoryId(categoryId).getData();
        }
        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuffer().append("%").append(keyword).append("%").toString();
        }
        logger.info("搜索关键词：" + keyword);
        PageHelper.startPage(pageNum, pageSize);//紧跟在这后面的第一个查询数据库方法会被分页
        if (StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArr = orderBy.split("_");
                PageHelper.orderBy(orderByArr[0] + " " + orderByArr[1]);
            }
        }
        List<Product> productList = productMapper.
                selectByKeywordAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);
        for (Product productItem : productList){
            productListVoList.add(assembleProductListItemDTO(productItem));
        }
        return ServerResponse.createBySuccess(new PageInfo(productListVoList));
    }
}
