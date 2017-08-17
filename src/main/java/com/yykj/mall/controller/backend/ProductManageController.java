package com.yykj.mall.controller.backend;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lee on 2017/8/17.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController{

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "save_or_update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveOrUpdate(Product product){
        return iProductService.saveOrUpdate(product);
    }

    @RequestMapping(value = "set_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setStatus(Integer productId, Integer status){
        return iProductService.setStatus(productId, status);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(Integer productId){
        return null;
    }
}
