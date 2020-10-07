package com.xybbz.controller;

import com.xybbz.entity.CommonResult;
import com.xybbz.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {

//    public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL = "http://CLOUD-PAYMEN-SERVICE";

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/insert", payment, CommonResult.class);
    }
    @GetMapping("/select")
    public CommonResult<Payment> selct(Long id) {
        return restTemplate.getForObject(PAYMENT_URL+ "/payment/inquiry?id=" + id, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get")
    public CommonResult<Payment> selct2(Long id) {
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/inquiry?id=" + id,
                CommonResult.class);
//        restTemplate.postForEntity(PAYMENT_URL + "/payment/insert", payment, CommonResult.class);
        if (forEntity.getStatusCode().is2xxSuccessful()) {
            return forEntity.getBody();
        } else {
            return new CommonResult<>(444,"操作失败");
        }
    }
}
