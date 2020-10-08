package com.xybbz.controller;

import com.xybbz.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.MessageFormat;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/hystrix/ok")
    public String payMentInfo_OK(Integer id) {
        String result = paymentService.payMentInfo_OK(id);
        log.info(MessageFormat.format("****result: {0}",result));
        return result;
    }

    @GetMapping("/payment/hystrix/timeout")
    public String payMentInfo_timOut(Integer id) {
        String result = paymentService.payMentInfo_timOut(id);
        log.info(MessageFormat.format("****result: {0}",result));
        return result;
    }

    //-----服务熔断
    @GetMapping("/payment/circuit")
    public String paymentCircuitBreaker(Long id) {
        String result = paymentService.paymentCircuitBreaker(id);
        log.info(MessageFormat.format("***result: {0}",result));
        return result;
    }
}
