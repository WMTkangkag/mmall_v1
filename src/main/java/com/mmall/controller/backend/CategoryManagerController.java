package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisSharedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by txk on 2018/5/31.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {

    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(@RequestParam(value = "parentId",defaultValue ="0") int parentId, String categoryName, HttpServletRequest httpServletRequest){
        //检验是否是管理员
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User currentUser= JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iCategoryService.addCategory(parentId,categoryName);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(int categoryId,String categoryName,HttpServletRequest httpServletRequest){
        //检验是否是管理员
        //User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User currentUser= JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if( iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iCategoryService.setCategoryName(categoryId,categoryName);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getCategory(@RequestParam(value = "categoryId",defaultValue ="0") int categoryId,HttpServletRequest httpServletRequest){
        //检验是否是管理员
        //User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User currentUser= JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if( iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
            return iCategoryService.getCategoryAndChildrenById(categoryId);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getDeepCategory(@RequestParam(value = "categoryId",defaultValue ="0") int categoryId,HttpServletRequest httpServletRequest){
        //检验是否是管理员
        //User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User currentUser= JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请重新登陆");
        }
        if( iUserService.checkAdminRole(currentUser).isSuccess()){
            //若是管理员才执行添加操作
             return iCategoryService.getCategoryAndDeepChlidrenById(categoryId);
        }else {
            return  ServerResponse.createByErrorMassage("不是管理员，没有权限");
        }
    }




}
