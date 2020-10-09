package com.xybbz.controller;


import com.xybbz.entity.CommonResult;
import com.xybbz.entity.Payment;
import com.xybbz.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.time.ZonedDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2020-10-06
 */
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/lb/insert")
    public CommonResult create(@RequestBody Payment payment) {
        boolean result = paymentService.save(payment);
        log.info(MessageFormat.format("****插入结果{0}",result));
        return result ? new CommonResult(200,"数据库插入成功" + serverPort,result) :
                new CommonResult(500,"数据库插入失败",result);
    }


    @GetMapping("/get/inquiry")
    public CommonResult getEntity(Long id) {
        id = 31L;
        Payment payment = paymentService.getById(id);
        int age = 1/10;
        if ( !StringUtils.isEmpty(payment)) {
            return new CommonResult(200,"查询成功" + serverPort, payment);
        }
        else {
            return  new CommonResult(500,"查询失败",null);
        }
    }

    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }

}
