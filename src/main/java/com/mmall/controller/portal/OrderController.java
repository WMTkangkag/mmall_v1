package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by txk on 2018/6/21.
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse pay(HttpServletRequest httpServletRequest, Integer shippingId) {
       //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.createOrder(user.getId(),shippingId);
    }


    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse<String> cancel(HttpServletRequest httpServletRequest,Long orderNo){
       // User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(),orderNo);
    }

    /**
     * 获取购物车中已经选中的商品详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse<String> getOrderCartProduct(HttpServletRequest httpServletRequest,Long orderNo){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderCartProduct(user.getId());
    }

    /**
     * 订单详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest httpServletRequest,Long orderNo){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest httpServletRequest,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }

















    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo, HttpServletRequest request) {
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path=request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }

    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String ,String> params= Maps.newHashMap();
        Map requestParams= request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name=(String) iter.next();
            String[] values=(String[]) requestParams.get(name);
            String valueStr="";
            for(int i=0;i<values.length;i++){
                //如果不是最后一个，若是则用“，”拼接
                valueStr=(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
        //非常重要,验证回调的正确性,是不是支付宝发的，并且还要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2= AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMassage("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }
        //todo 验证各种数据
        //
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCEESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }
    //前台轮询查看支付状态
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> querOrderPaystatus(HttpServletRequest httpServletRequest,Long orderNo){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse=iOrderService.querOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


}
