package com.yykj.mall.controller.backend;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.IFileService;
import com.yykj.mall.service.IProductService;
import com.yykj.mall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Lee on 2017/8/17.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController{

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

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
        return iProductService.manageProductDetail(productId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchProduct(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "file", required = false)MultipartFile file, HttpServletRequest req){

        String path = req.getSession().getServletContext().getRealPath("upload");//webapp目录
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("uri", targetFileName);
        resultMap.put("url", url);
        return ServerResponse.createBySuccess(resultMap);
    }
}
