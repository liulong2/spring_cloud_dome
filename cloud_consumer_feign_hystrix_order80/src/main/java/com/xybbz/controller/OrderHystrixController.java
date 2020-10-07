package com.xybbz.controller;

import com.xybbz.service.PaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok")
    public String payMentInfo_OK(@RequestParam("id") Integer id){
        return paymentHystrixService.payMentInfo_OK(id);
    }
    @GetMapping("/consumer/payment/hystrix/timeout")
    public String payMentInfo_timOut(@RequestParam("id") Integer id){
        return paymentHystrixService.payMentInfo_timOut(id);
    }
}
