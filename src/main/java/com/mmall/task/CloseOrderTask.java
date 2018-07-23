package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisSharedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by txk on 2018/7/22.
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

   // @Scheduled(cron = "0 */1 * * * ?")//每1分钟
    public void closeOrderTaksV1(){
        log.info("关闭订单定时任务启动");
        int hour=Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        //关闭以当前时间为准的2个小时之前的所有订单，不管是谁的
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")//每1分钟
    public void closeOrderTaksV2(){
        log.info("关闭订单定时任务启动");
        //int hour=Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        Long timeout=Long.parseLong(PropertiesUtil.getProperty("lock.timeout"));

        Long setnxResult= RedisSharedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+timeout));
        if(setnxResult!=null && setnxResult.intValue()==1){
            //如果返回值是1，代表设置成功，获取锁,只有线程获取了锁才能执行关单任务，其他线程不能执行
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务结束");
        //关闭以当前时间为准的2个小时之前的所有订单，不管是谁的
        //iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }
    private void closeOrder(String lockName){
        RedisSharedPoolUtil.expire(lockName,50);//有效期50秒，防止死锁。
        log.info("获取{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());//打印日志代表哪个线程获取了这个锁
        //定时关单时间
        int hour=Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        //执行完了，也关闭完订单了，即使没有超过50秒，不能让他等待就要释放锁
        RedisSharedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("===================================");

    }
}