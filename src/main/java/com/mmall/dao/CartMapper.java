package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectByUserId(Integer userId);

    int countAllUnChecked(Integer userId);

    Cart selectByUserIdAndProductId(@Param(value = "userId")Integer userId,@Param(value = "productId")Integer productId);

    int deleteByProductListAndUserId(@Param(value = "userId")Integer userId,@Param(value = "productList")List<String> productList);

    int checkOrUncheckProduct(@Param(value = "userId") Integer userId,
                              @Param(value = "checked") Integer checked,
                              @Param(value ="productId") Integer productId);

    int selectCartProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);

}