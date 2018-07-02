package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.List;

/**
 * Created by txk on 2018/5/31.
 */
public interface ICategoryService {

    ServerResponse addCategory(Integer parentId,String categoryName);

    ServerResponse setCategoryName(Integer categoryId,String categoryName);

    ServerResponse getCategoryAndChildrenById(Integer categoryId);

    ServerResponse<List<Integer>> getCategoryAndDeepChlidrenById(Integer categoryId);
}
