package com.yykj.mall.controller.backend;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Category;
import com.yykj.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Lee on 2017/8/15.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId){
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "modify_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse modifyCategory(Integer categoryId, String newCategoryName){
        return iCategoryService.modifyCategory(categoryId, newCategoryName);
    }

    /**
     * 找出父分类的子分类（平级子分类）
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_parallel_children_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getParallelChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        return iCategoryService.getParallelChildrenCategory(categoryId);
    }

    @RequestMapping(value = "get_deep_children_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        return iCategoryService.getDeepChildrenCategory(categoryId);
    }
}