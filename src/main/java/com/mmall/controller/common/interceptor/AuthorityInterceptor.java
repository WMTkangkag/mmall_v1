package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import com.mmall.util.RedisSharedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by txk on 2018/7/17.
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    //调用controller之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        //返回值决定是否返回posthandler
        //请求中的方法名
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        //解析HandlerMethod
        String methodName=handlerMethod.getMethod().getName();
        String className=handlerMethod.getBean().getClass().getSimpleName();
        //解析具体的参数，具体的key和value是什么，打印日志
        StringBuffer sb=new StringBuffer();
        Map paramMap=request.getParameterMap();
        Iterator iterator=paramMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry) iterator.next();
            String mapKey=(String) entry.getKey();
            String mapValue= StringUtils.EMPTY;
            //request这个参数的map，里面的value是一个String[];
            Object value = entry.getValue();
            if(value instanceof String[]){
                mapValue= Arrays.toString((String[]) value);
            }
            sb.append(mapKey).append("=").append(mapValue);
        }
        User user=null;
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isNotEmpty(loginToken)){
            //获取到用户信息
            String currentUser= RedisPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(currentUser, User.class);
        }
        if(user==null || (user.getRole().intValue()!= Const.Role.ROLE_ADMIN)){
            //返回false.即不会调用controller里的方法，直接返回浏览器这个时候就可以通过拦截器绑定我们的要返回的错误信息到前段页面
            //当用户为空，或者用户不为管理员的时候就返回false
            //重置一下response，
            response.reset();//这里要添加reset，否则报异常 getWriter() has already been called for this response.
            response.setCharacterEncoding("UTF-8");//这里要设置编码，否则会乱码
            response.setContentType("application/json;charset=UTF-8");//这里要设置返回值的类型，因为全部是json接口
            PrintWriter out = response.getWriter();
            if(user==null){
                //判断是否是富文本上传
                if(StringUtils.equals(className,"ProductManagerController")&&StringUtils.equals(methodName,"richTextImgupload")){
                    Map map= Maps.newHashMap();
                    map.put("success",false);
                    map.put("msg","用户未登陆");
                    out.print(JsonUtil.obj2String(map));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMassage("拦截器拦截，用户未登陆")));
                }
            }else {
                if(StringUtils.equals(className,"ProductManagerController")&&StringUtils.equals(methodName,"richTextImgupload")){
                    Map map= Maps.newHashMap();
                    map.put("success",false);
                    map.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(map));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMassage("拦截器拦截，无权限操作")));
                }
            }
            out.flush();//将out流中的数据清空掉
            out.close();//这里要关闭
            return false;
        }
        return true;
    }

    @Override
    //调用controller之后
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
