package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import com.sun.corba.se.impl.oa.poa.POACurrent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by txk on 2018/7/6.
 */
public class RedisPool {

    private static JedisPool pool;//jedis连接池
    private static Integer maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.max.tatol","20"));//最大连接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//在jedispool中最大的idle（最空闲状态大）状态的jedis实例个数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//在jedispool中最小的idle状态的jedis个数
    private static Boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值为true，则得到的jedis实例是肯定能用的
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在Returen一个jedis实例的时候，是否要进行验证操作，如果赋值为true，则返回的jedis实例是肯定能用的

    private static String redisIp=PropertiesUtil.getProperty("redis1.ip");
    private static Integer redisPort=Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static void initPool(){

        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽时，是否阻塞，false会抛出异常

        pool=new JedisPool(config,redisIp,redisPort,1000*2);
    }

    static {
        initPool();
    }

    //返回jedis实例
    public static Jedis getJedis(){
        return pool.getResource();
    }

    //把jedis放回连接池
    public static void returnResource(Jedis jedis){
            pool.returnResource(jedis);
    }
    //把损坏的jedis实例放回连接池
    // （在testOnBorrow和testOnreturn的时候默认值是false但是在高并发的时候，有判断放回正常的resource和brokenresourc，
    // 调用jedis的一些方法，发现发生异常就可以把jedis放入一个brokenresource里面,提高连接效率）
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("kangkangkey","kangkangvalue");
        returnResource(jedis);
        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }
}

