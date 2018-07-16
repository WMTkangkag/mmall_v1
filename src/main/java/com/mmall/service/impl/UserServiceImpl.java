package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.util.RedisPoolUtil;
import com.sun.corba.se.spi.activation.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by txk on 2018/5/28.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    public ServerResponse<User> login(String username, String password) {
        //首先验证用户名是否存在
        int usernameExist = userMapper.checkUsernameExist(username);
        if (usernameExist == 0) {
            return ServerResponse.createByErrorMassage("用户名不存在");
        }
        //用户密码md加密
        password = MD5Util.MD5EncodeUtf8(password);
        //验证用户密码是否正确
        User user = userMapper.selectUserByUsernameAndPwd(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMassage("用户密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }


    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int i = userMapper.insert(user);
        if (i == 0) {
            return ServerResponse.createByErrorMassage("注册失败");
        }
        return ServerResponse.createBySuccessMassage("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int usernameExist = userMapper.checkUsernameExist(str);
                if (usernameExist > 0) {
                    return ServerResponse.createByErrorMassage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int checkEmail = userMapper.checkEmail(str);
                if (checkEmail > 0) {
                    return ServerResponse.createByErrorMassage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMassage("参数错误");
        }
        return ServerResponse.createBySuccessMassage("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validRespond = this.checkValid(username, Const.USERNAME);
        if (validRespond.isSuccess()) {
            return ServerResponse.createByErrorMassage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        return ServerResponse.createBySuccess(question);
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int checkAnswer = userMapper.checkAnswer(username, question, answer);
        if (checkAnswer == 0) {
            return ServerResponse.createByErrorMassage("问题答案错误");
        }
        String token = UUID.randomUUID().toString();
        RedisPoolUtil.setEx(Const.TOKEN_PREFIX + username,token,60*60*12);
        return ServerResponse.createBySuccess(token);
    }

    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMassage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMassage("用户不存在");
        }
        String token =RedisPoolUtil.get(Const.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMassage("token已经失效");
        }
        if (!StringUtils.equals(forgetToken, token)) {
            return ServerResponse.createByErrorMassage("token验证错误，请重新获取重置密码的token");
        }
        passwordNew = MD5Util.MD5EncodeUtf8(passwordNew);
        int rowcount = userMapper.updatePasswordByUsername(username, passwordNew);
        if (rowcount > 0) {
            return ServerResponse.createBySuccessMassage("密码修改成功");
        }
        return ServerResponse.createByErrorMassage("密码修改失败");
    }


    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {

        passwordOld=MD5Util.MD5EncodeUtf8(passwordOld);
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int countResult = userMapper.checkPassword(passwordOld, user.getId());
        if(countResult==0){
            return ServerResponse.createByErrorMassage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateResult = userMapper.updateByPrimaryKey(user);
        if(updateResult>0){
            return ServerResponse.createBySuccessMassage("密码更新成功");
        }
        return ServerResponse.createByErrorMassage("密码更新失败");
    }

    public ServerResponse<User> updateInfomation(User user) {

        //用户名不能更新
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int emailCount = userMapper.checkEmailById(user.getEmail(), user.getId());
        if(emailCount>0){
            return ServerResponse.createByErrorMassage("邮箱已经存在，请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerResponse.createBySuccess("个人信息更新成功",user);
        }
        return ServerResponse.createByErrorMassage("个人信息更新失败");
    }

    public  ServerResponse<User> getInformation(Integer id){
        User currentUser = userMapper.selectByPrimaryKey(id);
        if(currentUser==null){
            ServerResponse.createByErrorMassage("找不到当前用户");
        }
        currentUser.setPassword(StringUtils.EMPTY);
        return  ServerResponse.createBySuccess("获取个人信息成功",currentUser);
    }

    //校验是否是管理员
    public ServerResponse checkAdminRole(User user){
        if(user!=null && user.getRole()==Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}

