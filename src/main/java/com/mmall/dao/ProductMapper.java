package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getProductList();

    List<Product> searchProductList(@Param(value = "productName") String  productName,@Param(value = "productId") Integer productId);

    List<Product> selectBykeywordAndCategoryIds(@Param(value = "keyword")String keyword,@Param(value ="categoryIdList")List<Integer> categoryIdList);

    /*这里一定要用Integer，考虑到删除产品表中该商品会返回一个null*/
    Integer selectStockByProductId(Integer id);

    int closeOrderByorderId(Integer id);

}