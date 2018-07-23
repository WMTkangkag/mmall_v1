package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by txk on 2018/6/4.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse saveOrUpdate(HttpServletRequest httpServletRequest, Product product){
        /*//User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            ServerResponse response = iProductService.saveOrUpdateProduct(product);
            return response;
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }*/
        //全部通过拦截器进行权限认证
        ServerResponse response = iProductService.saveOrUpdateProduct(product);
        return response;

    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest httpServletRequest,Integer productId,Integer status){
       /* //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }*/
        //全部通过拦截器进行权限认证
        return iProductService.setSaleStatus(productId,status);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(HttpServletRequest httpServletRequest,Integer productId){
        /*//User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.manageGetProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }*/
        //全部通过拦截器进行权限认证
        return iProductService.manageGetProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        /*//User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
           return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }*/
        //全部通过拦截器进行权限认证
        return iProductService.getProductList(pageNum,pageSize);
    }


    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest httpServletRequest,String productName,Integer productId ,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        /*//User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProductList(productName,productId,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }*/
        //全部通过拦截器进行权限认证
        return iProductService.searchProductList(productName,productId,pageNum,pageSize);
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file",required =false) MultipartFile file, HttpServletRequest request){
        /*//User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map map=Maps.newHashMap();
            map.put("uri",targetFileName);
            map.put("url",url);
            return ServerResponse.createBySuccess(map);

        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }*/
        //全部通过拦截器进行权限认证
        Map map=Maps.newHashMap();
        String path=request.getSession().getServletContext().getRealPath("upload");//获取服务器上upload的绝对路径
        String targetFileName = iFileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            map.put("success",false);
            map.put("msg","上传失败");
        }
        String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        map.put("uri",targetFileName);
        map.put("url",url);
        return ServerResponse.createBySuccess(map);
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richTextImgupload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file",required =false) MultipartFile file, HttpServletRequest request){
       /* //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            Map map=Maps.newHashMap();
            map.put("success",false);
            map.put("msg","用户未登陆");
            return map;
        }
        String userJsonStr= RedisSharedPoolUtil.get(loginToken);
        User user= JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            Map map=Maps.newHashMap();
            map.put("success",false);
            map.put("msg","用户未登陆");
            return map;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map map=Maps.newHashMap();
            map.put("success",true);
            map.put("msg","上传成功");
            map.put("file_path",url);
            return map;
        }else {
            Map map=Maps.newHashMap();
            map.put("success",false);
            map.put("msg","无权限");
            return map;
        }*/
        //全部通过拦截器进行权限认证
        Map map=Maps.newHashMap();
        String path=request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            map.put("success",false);
            map.put("msg","上传失败");
        }
        String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        map.put("success",true);
        map.put("msg","上传成功");
        map.put("file_path",url);
        return map;
    }










}
