package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdAndUserId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    int updateByShipping(Shipping record);

    Shipping selectByUserIdAndShippingId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    List<Shipping> selectPageByUserId(Integer userId);

}