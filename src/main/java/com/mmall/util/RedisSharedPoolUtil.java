package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisSharedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * jedis api的封装
 * Created by txk on 2018/7/6.
 */
@Slf4j
public class RedisSharedPoolUtil {

    public static String set(String key,String value){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis= RedisSharedPool.getJedis();
            result=jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis= RedisSharedPool.getJedis();
            result=jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
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
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis= RedisSharedPool.getJedis();
            result=jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    public static Long setnx(String key,String value){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis= RedisSharedPool.getJedis();
            result=jedis.setnx(key,value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{} error",key,value);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }


    /**
     * 设置key的有效时间，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis= RedisSharedPool.getJedis();
            result=jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("setex key:{} error",key);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis= RedisSharedPool.getJedis();
            result=jedis.del(key);
        } catch (Exception e) {
            log.error("setex key:{} error",key);
            RedisSharedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisSharedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest","value");

        String value= RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEx("keyex","keyvalues",60*10);

        RedisPoolUtil.setEx("keyTest","value",60*20);
        RedisPoolUtil.del("keyTest");
    }


}
