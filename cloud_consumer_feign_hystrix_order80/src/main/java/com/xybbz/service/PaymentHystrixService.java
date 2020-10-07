package com.xybbz.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("CLOUD-PROVIDER-HYSTRIX-PAYMENT")

public interface PaymentHystrixService {

    @GetMapping("/payment/hystrix/ok")
    public String payMentInfo_OK(@RequestParam("id") Integer id);

    @GetMapping("/payment/hystrix/timeout")
    public String payMentInfo_timOut(@RequestParam("id") Integer id);
}
