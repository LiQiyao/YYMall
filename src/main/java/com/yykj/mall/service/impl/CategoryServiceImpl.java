package com.yykj.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.dao.CategoryMapper;
import com.yykj.mall.pojo.Category;
import com.yykj.mall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Lee on 2017/8/15.
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, int parentId){
        if (StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加分类参数错误！");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int res = categoryMapper.insertSelective(category);
        if (res > 0){
            return ServerResponse.createBySuccess("添加分类成功！");
        }
        return ServerResponse.createByErrorMessage("添加分类失败！");
    }

    @Override
    public ServerResponse modifyCategory(Integer categoryId, String newCategoryName){
        if (categoryId == null || StringUtils.isBlank(newCategoryName)){
            ServerResponse.createByErrorMessage("修改分类名称参数错误！");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(newCategoryName);
        int res = categoryMapper.updateByPrimaryKeySelective(category);
        if (res > 0){
            return ServerResponse.createBySuccessMessage("更新分类名称成功！");
        }
        return ServerResponse.createByErrorMessage("更新分类名称失败！");
    }

    @Override
    public ServerResponse<List<Category>> getParallelChildrenCategory(Integer parentId) {
        List<Category> categoryList = categoryMapper.selectByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Category>> getDeepChildrenCategory(Integer parentId) {
        Set<Category> categorySet = Sets.newHashSet();
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if (category != null){
            categorySet.add(category);
        }
        findChildrenCategory(categorySet, parentId);
        List<Category> categoryList = Lists.newArrayList();
        categoryList.addAll(categorySet);
        return ServerResponse.createBySuccess(categoryList);
    }

    //不同于上一个方法的是只查Id
    @Override
    public ServerResponse<List<Integer>> getDeepChildrenCategoryId(Integer parentId) {
        Set<Category> categorySet = Sets.newHashSet();
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if (category != null){
            categorySet.add(category);
        }
        findChildrenCategory(categorySet, parentId);
        List<Integer> categoryIdList = Lists.newArrayList();
        for (Category categoryItem : categorySet){
            categoryIdList.add(categoryItem.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private void findChildrenCategory(Set<Category> categorySet, int categoryId){
            List<Category> categoryList = categoryMapper.selectByParentId(categoryId);
            for (Category item : categoryList){
                categorySet.add(item);
                findChildrenCategory(categorySet, item.getId());
            }
    }

}
