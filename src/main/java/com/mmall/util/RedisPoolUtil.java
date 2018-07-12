package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * jedis api的封装
 * Created by txk on 2018/7/6.
 */
@Slf4j
public class RedisPoolUtil {

    public static String set(String key,String value){
        Jedis jedis=null;
        String result=null;
        try {
            jedis= RedisPool.getJedis();
            result=jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis=null;
        String result=null;
        try {
            jedis= RedisPool.getJedis();
            result=jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     *设置key vaule
     * @param key
     * @param value
     * @param exTime 存的是秒
     * @return
     */
    public static String setEx(String key,String value,int exTime){
        Jedis jedis=null;
        String result=null;
        try {
            jedis= RedisPool.getJedis();
            result=jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key的有效时间，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis=null;
        Long result=null;
        try {
            jedis= RedisPool.getJedis();
            result=jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("setex key:{} error",key);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis=null;
        Long result=null;
        try {
            jedis= RedisPool.getJedis();
            result=jedis.del(key);
        } catch (Exception e) {
            log.error("setex key:{} error",key);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest","value");

        String value=RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEx("keyex","keyvalues",60*10);

        RedisPoolUtil.setEx("keyTest","value",60*20);
        RedisPoolUtil.del("keyTest");
    }

}
