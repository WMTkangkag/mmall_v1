package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by txk on 2018/6/12.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 新建地址方法
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount>0){
            return ServerResponse.createBySuccess("新建地址成功",shipping.getId());
        }
        return ServerResponse.createByErrorMassage("新建地址失败");

    }

    /**
     * 删除地址方法
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse<String> del(Integer userId, Integer shippingId){
        int rowCount = shippingMapper.deleteByShippingIdAndUserId(userId,shippingId);//会出现横向越权
        if(rowCount>0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMassage("删除地址失败");
    }

    /**
     *更新地址方法
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);//为了防止横向越权将userid和shipping组合起来
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMassage("更新地址失败");
    }

    /**
     * 查询地址方法
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);//会出现横向越权
        if(shipping==null){
            return ServerResponse.createByErrorMassage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功",shipping);
    }

    /**
     * 分页查询订单
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList= shippingMapper.selectPageByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }



}
