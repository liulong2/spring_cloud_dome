package com.xybbz.controller;


import com.xybbz.entity.CommonResult;
import com.xybbz.entity.Payment;
import com.xybbz.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

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

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping("/insert")
    public CommonResult create(@RequestBody Payment payment) {
        boolean result = paymentService.save(payment);
        log.info(MessageFormat.format("****插入结果{0}",result));
        return result ? new CommonResult(200,"数据库插入成功" + serverPort,result) :
                new CommonResult(500,"数据库插入失败",result);
    }


    @GetMapping("/inquiry")
    public CommonResult getEntity(Long id) {
        Payment payment = paymentService.getById(id);
        int age = 1/10;
        if ( !StringUtils.isEmpty(payment)) {
            return new CommonResult(200,"查询成功" + serverPort, payment);
        }
        else {
            return  new CommonResult(500,"查询失败",null);
        }
    }


    @GetMapping("/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        services.forEach(string ->{
            log.info(MessageFormat.format("######element: {0}",string));
        });
        //通过微服务名称获得微服务全部实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMEN-SERVICE");
        instances.forEach(serviceInstance -> {
            log.info(serviceInstance.getInstanceId() + "\t"
                    + serviceInstance.getHost() + "\t" + serviceInstance.getPort() + "\t" + serviceInstance.getUri());
        });
        return this.discoveryClient;
    }

}
