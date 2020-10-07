package com.xybbz.controller;

import com.xybbz.entity.CommonResult;
import com.xybbz.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
@Slf4j
public class OrderFeignController {

    @Autowired
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/payment/inquiry")
    public CommonResult getEntity(Long id) {
        return paymentFeignService.getEntity(id);
    }

}
