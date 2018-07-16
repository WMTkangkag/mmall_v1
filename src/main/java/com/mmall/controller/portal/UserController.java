package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.RedisPool;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by txk on 2018/5/28.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录方法
     * @param username
     * @param password
     * @param session
     * @return
     */
     @RequestMapping(value = "login.do",method = RequestMethod.POST)
     @ResponseBody
     public ServerResponse<User> login(String username , String password, HttpSession session, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
         ServerResponse<User> response = iUserService.login(username, password);
         //如果返回的是正确信息就把客户信息放到session域中
         if(response.isSuccess()){
             CookieUtil.writeLoginToken(httpServletResponse,session.getId());
             RedisPoolUtil.setEx(session.getId(),JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
         }
         return  response;
     }

    /**
     * 用户退出方法
     * @param session
     * @return
     */
    @RequestMapping(value ="logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.delLoginToken(httpServletRequest,httpServletResponse);
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccessMassage("用户退出成功");
    }

    /**
     * 用户注册方法
     * @param user
     * @return
     */
    @RequestMapping(value ="register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        ServerResponse<String> response = iUserService.register(user);
        return  response;
    }

    /**
     * 验证方法
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value ="check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type) {

        ServerResponse<String> response = iUserService.checkValid(str, type);
        return response;
    }

    /***
     * 获取用户登录方法
     * @param session
     * @return
     */
    @RequestMapping(value ="get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
    }

    /**
     * 忘记密码提示方法
     * @param username
     * @return
     */
    @RequestMapping(value ="forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){

        ServerResponse<String> response = iUserService.selectQuestion(username);
        return response;
    }

    /**
     * 忘记密码问题检验
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value ="forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        ServerResponse<String> response = iUserService.checkAnswer(username, question, answer);
        return response;
    }

    /**
     * 忘记密码的重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value ="forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){

        ServerResponse<String> response = iUserService.forgetResetPassword(username, passwordNew, forgetToken);
        return response;
    }


    /**
     * 登陆状态重置密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping(value ="reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,HttpServletRequest httpServletRequest){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User user=JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null){
            return  ServerResponse.createByErrorMassage("用户未登陆");
        }
        ServerResponse<String> response = iUserService.resetPassword(passwordOld, passwordNew, user);
        return response;
    }

    /**
     * 登陆状态下的更新个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value ="update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfomation(HttpServletRequest httpServletRequest,User user){
        //User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User currentUser=JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser==null){
            return ServerResponse.createByErrorMassage("用户未登陆");
        }
        //user从前端传递过来时无userid，防止越权，从前端传过来的id被改变，从而改变别的user数据
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInfomation(user);
        if(response.isSuccess()) {
            RedisPoolUtil.set(loginToken, JsonUtil.obj2String(response.getData()));
            //session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;

    }

    /**
     * 登陆状态获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value ="get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpServletRequest httpServletRequest){
        //User currentUser= (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorMassage("用户未登陆，无法获取用户登录信息");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        User currentUser=JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser==null){
            return ServerResponse.createByErrorMassage("用户未登陆");
        }
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,无法获取当前用户信息,status=10,强制登录");
        }
        ServerResponse<User> response=iUserService.getInformation(currentUser.getId());
        return response;
    }



}
