package com.yykj.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.service.IProductService;
import com.yykj.mall.dto.ProductDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lee on 2017/8/19.
 */
@RequestMapping("/product/")
@Controller
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "{productId}/detail.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailDTO> detail(@PathVariable Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "list.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
