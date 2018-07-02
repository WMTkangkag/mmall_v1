package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by txk on 2018/5/31.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(Integer parentId,String categoryName){

        if(parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMassage("添加品类参数错误");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int rowCount= categoryMapper.insert(category);
        if(rowCount>0){
            return ServerResponse.createBySuccessMassage("添加品类成功");
        }
        return ServerResponse.createByErrorMassage("添加品类失败");
    }


    public  ServerResponse setCategoryName(Integer categoryId,String categoryName){
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return  ServerResponse.createByErrorMassage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccessMassage("更新品类名成功");
        }
        return ServerResponse.createByErrorMassage("更新品类名失败");
    }

    public ServerResponse getCategoryAndChildrenById(Integer categoryId){
        if(categoryId==null){
            return  ServerResponse.createByErrorMassage("查询品类子节点参数错误");
        }
        List<Category> categoryList = categoryMapper.selectByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子信息");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse<List<Integer>> getCategoryAndDeepChlidrenById(Integer categoryId){
        if(categoryId==null){
            return  ServerResponse.createByErrorMassage("查询品类子节点参数错误");
        }
        HashSet<Category> categorySet =Sets.newHashSet();
        List<Integer> categories = Lists.newArrayList();
        currSelectChlidrenCategory(categorySet,categoryId);
        for (Category c:categorySet) {
             categories.add(c.getId());
        }
        return ServerResponse.createBySuccess(categories);
    }

    public void currSelectChlidrenCategory(Set<Category> categorySet,Integer categoryId){
        //获取父品类
        Category pCategory= categoryMapper.selectByPrimaryKey(categoryId);
        if (pCategory!=null){
            categorySet.add(pCategory);
        }
        List<Category> childrenCategories = categoryMapper.selectByParentId(categoryId);
        for (Category category:childrenCategories) {
                if(category!=null) {
                    currSelectChlidrenCategory(categorySet,category.getId());
                }
            }
        }

}
