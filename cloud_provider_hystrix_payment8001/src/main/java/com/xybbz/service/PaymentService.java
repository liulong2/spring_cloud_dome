package com.xybbz.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    /**
     * 正常访问方法
     *
     * @param id
     * @return
     */
    public String payMentInfo_OK(Integer id) {
        return "线程池: " + Thread.currentThread().getName() + " payMentInfo_OK" + id + "哈哈哈";
    }

    //添加熔断注解,fallbackMethod 表示兜底方法
    @HystrixCommand(fallbackMethod = "payMentInfo_TimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String payMentInfo_timOut(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       int i = 10/0;
        return "线程池: " + Thread.currentThread().getName() + " payMentInfo_timOut" + id + "哈哈哈" + "我是你爸";
    }

    public String payMentInfo_TimeoutHandler(Integer id) {
        return "线程池: " + Thread.currentThread().getName() + " payMentInfo_TimeoutHandler" + id + "你超时死了";
    }

    //----------服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期 重试睡眠多久
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")//失败率打到多少后跳闸
    })
    public String paymentCircuitBreaker(Long id){
        if (id < 0){
            throw new RuntimeException("*****id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        System.out.println(serialNumber);

        return Thread.currentThread().getName()+"\t"+"调用成功,流水号："+serialNumber;
    }
    public String paymentCircuitBreaker_fallback(Long id){
        return "id 不能负数，请稍候再试,(┬＿┬)/~~     id: " +id;
    }
}
