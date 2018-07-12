package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by txk on 2018/7/12.
 */@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN=".happymmall.com";
    private final static String COOKIE_NAME="mmall_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks=request.getCookies();
        if(cks!=null){
            for (Cookie ck:cks) {
                log.info("cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck=new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录
        ck.setMaxAge(60*60*2*365);//如果是-1，代表永久，如不设置就不会写入硬盘。而是写在内层，只在当前页面有效。
        log.info("write cookiename:{} ,cookievalue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);

    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks=request.getCookies();
        if(cks!=null){
            for (Cookie ck:cks) {
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);//设置成0，代表删除此cookie
                    log.info("del cookieName:{} ,cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }

    }
}
