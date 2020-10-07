package com.xybbz.service;

import com.xybbz.entity.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "CLOUD-PAYMEN-SERVICE")
public interface PaymentFeignService {

    @GetMapping("/payment/inquiry")
    public CommonResult getEntity(@RequestParam("id") Long id);
}
