package com.yykj.mall.controller.portal;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.entity.Category;
import com.yykj.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Lee
 * @date 2017/12/5
 */
@Controller
@RequestMapping("/categories/")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "parent/{parentId}/list.json", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getCategoryListByParentId(@PathVariable Integer parentId){
        return categoryService.getParallelChildrenCategory(parentId);
    }
}
