package com.xybbz.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xybbz.service.PaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok")
    public String payMentInfo_OK(@RequestParam("id") Integer id){
        return paymentHystrixService.payMentInfo_OK(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout")
//    @HystrixCommand(fallbackMethod = "payMentInfo_TimeoutHandler", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
//    })
    @HystrixCommand
    public String payMentInfo_timOut(@RequestParam("id") Integer id){
        int i = 10/0;
        return paymentHystrixService.payMentInfo_timOut(id);
    }
    public String payMentInfo_TimeoutHandler(Integer id) {
        return "线程池: " + Thread.currentThread().getName() + " payMentInfo_TimeoutHandler" + id + "你超时死了";
    }
    public String payment_Global_FallbackMethod(){
        return "Global异常处理信息，请稍后再试,(┬＿┬)";
    }

}
