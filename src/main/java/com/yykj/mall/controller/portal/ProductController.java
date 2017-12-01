package com.yykj.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.service.IProductService;
import com.yykj.mall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Lee on 2017/8/19.
 */
@RequestMapping("/product/")
@Controller
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "detail.html", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "list.html", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
