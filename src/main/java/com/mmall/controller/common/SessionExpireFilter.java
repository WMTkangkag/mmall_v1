package com.mmall.controller.common;


import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisSharedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by txk on 2018/7/13.
 */
public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        //从request中获取获取loginToken
        String loginToken= CookieUtil.readLoginToken(httpServletRequest);
        //如果loginToken不为空
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisSharedPoolUtil.get(loginToken);
            User user= JsonUtil.string2Obj(userJsonStr, User.class);
            if(user!=null){
                //如果user不为空，则重置session的时间，即调用expire命令
                RedisSharedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
