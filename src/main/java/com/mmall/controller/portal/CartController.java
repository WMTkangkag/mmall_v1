package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisSharedPoolUtil;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by txk on 2018/6/8.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> addProduct2Cart(HttpServletRequest httpServletRequest, Integer productId, Integer count){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.addProduct2Cart(user.getId(),productId,count);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> getCartList(HttpServletRequest httpServletRequest){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> updateCartProduct(HttpServletRequest httpServletRequest,Integer productId,Integer count){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest,String productIds){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }

    //全选
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelectAll(user.getId(),Const.Cart.CHECKED,null);
    }

    //全不选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelectAll(user.getId(),Const.Cart.UN_CHECKED,null);
    }

    //单选
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest,Integer productId){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelectAll(user.getId(),Const.Cart.CHECKED,productId);
    }

    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest,Integer productId){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelectAll(user.getId(),Const.Cart.UN_CHECKED,productId);
    }
    //获取购物车产品数量
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }











}
