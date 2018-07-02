package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by txk on 2018/5/29.
 */
public class TokenCache{
    //创建日志对象
    public static Logger logger= LoggerFactory.getLogger(TokenCache.class);
    public static String TOKEN_PREFIX="token_";
    //创建一个初始容量1000最大容量10000，数据存活时间为12小时的本地Guava缓存对象
    private static final LoadingCache<String,String> localCache= CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        @Override
        //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载.
        public String load(String s) throws Exception {
            return "null";
        }
    });
    public static void setkey(String key,String value){
        localCache.put(key,value);
    }
    public static String getValue(String key){
        String value=null;
        try {
            value= localCache.get(key);
            if("null".equals(value)){
                return null;
            }
                return value;
        } catch (Exception e) {
           logger.error("localCache get error",e);
        }
        return null;
    }

}
