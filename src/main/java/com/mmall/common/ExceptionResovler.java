package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by txk on 2018/7/17.
 */
@Slf4j
//非service成和Resportity成的个用这个
@Component
public class ExceptionResovler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        log.error("{} Exception",httpServletRequest.getRequestURI(),e);
        //使用ModelAndView（View view）的构造器，当使用jackson2.0的时候，就使用MappingJackson2JsonView
        ModelAndView modelAndView=new ModelAndView(new MappingJacksonJsonView());
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情请查看服务器端的日志的信息");
        modelAndView.addObject("data",e.toString());
        return modelAndView;
    }
}
