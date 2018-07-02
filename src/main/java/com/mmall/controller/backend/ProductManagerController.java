package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ServerResponse saveOrUpdate(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            ServerResponse response = iProductService.saveOrUpdateProduct(product);
            return response;
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.manageGetProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
           return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }
    }


    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,String productName,Integer productId ,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProductList(productName,productId,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMassage("该用户没有权限");
        }
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required =false) MultipartFile file, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
        }
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richTextImgupload(HttpSession session,@RequestParam(value = "upload_file",required =false) MultipartFile file, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
        }
    }










}
