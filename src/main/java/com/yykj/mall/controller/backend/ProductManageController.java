package com.yykj.mall.controller.backend;

import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Product;
import com.yykj.mall.service.IFileService;
import com.yykj.mall.service.IProductService;
import com.yykj.mall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "save_or_update.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveOrUpdate(Product product){
        return iProductService.saveOrUpdate(product);
    }

    @RequestMapping(value = "set_status.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setStatus(Integer productId, Integer status){
        return iProductService.setStatus(productId, status);
    }

    @RequestMapping(value = "detail.html", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(Integer productId){
        return iProductService.manageProductDetail(productId);
    }

    @RequestMapping(value = "list.html", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "search.html", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchProduct(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping(value = "upload.html", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false)MultipartFile file, HttpServletRequest req){

        String path = req.getSession().getServletContext().getRealPath("upload");//webapp目录
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)){
            return ServerResponse.createByErrorMessage("上传失败！");
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("uri", targetFileName);
        resultMap.put("url", url);
        return ServerResponse.createBySuccess(resultMap);
    }

    //富文本的返回格式要求
/*    {
        "success": true/false,
        "msg": "error message", # optional
        "file_path": "[real file path]"
    }*/
    @RequestMapping(value = "rich_text_upload.html", method = RequestMethod.POST)
    @ResponseBody
    public Map richTextUpload(@RequestParam(value = "upload_file", required = false)MultipartFile file, HttpServletRequest req, HttpServletResponse resp){
        Map resultMap = Maps.newHashMap();
        String path = req.getSession().getServletContext().getRealPath("upload");//项目根目录下的upload
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)){
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("url", url);
        resp.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
