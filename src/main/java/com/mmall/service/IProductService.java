package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * Created by txk on 2018/6/4.
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId,Integer status);

    ServerResponse manageGetProductDetail(Integer productId);

    ServerResponse getProductList(Integer pageNum,Integer pageSize);

    ServerResponse searchProductList(String  productName,Integer productId,Integer pageNum,Integer pageSize);

    ServerResponse getProductDetail(Integer productId);

    ServerResponse searchProductByKeywordAndCategoryId(Integer categoryId,String keyword, int pageNum, int pageSize, String orderBy);

}
