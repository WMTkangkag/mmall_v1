package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redission的初始化类
 * Created by txk on 2018/7/24.
 */
@Component
@Slf4j
public class RedissionManager {

   private Config config=new Config();

    private Redisson redisson=null;

    public Redisson getRedisson() {
        return redisson;
    }

    private static String redis1Ip= PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port=Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip=PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port=Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    @PostConstruct//在构造器完成之后会加载这个方法
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());
            redisson= (Redisson) Redisson.create(config);
            log.info("初始化Redission结束");
        } catch (Exception e) {
            log.error("redission init error",e);
            e.printStackTrace();
        }


    }


}
