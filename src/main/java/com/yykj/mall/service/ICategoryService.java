package com.yykj.mall.service;

import com.yykj.mall.common.ServerResponse;
import com.yykj.mall.pojo.Category;

import java.util.List;

/**
 * Created by Lee on 2017/8/15.
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, int parentId);

    ServerResponse modifyCategory(Integer categoryId, String newCategoryName);

    ServerResponse<List<Category>> getParallelChildrenCategory(Integer parentId);

    ServerResponse<List<Category>> getDeepChildrenCategory(Integer parentId);

    ServerResponse<List<Integer>> getDeepChildrenCategoryId(Integer parentId);
}
