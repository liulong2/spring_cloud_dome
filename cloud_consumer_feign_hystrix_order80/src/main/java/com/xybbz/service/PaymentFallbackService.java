package com.xybbz.service;

import org.springframework.stereotype.Component;

/**
 * @author 刘梦龙22
 * @create 2020-10-08
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String payMentInfo_OK(Integer id) {
        return "服务失败了,你个大傻子:payMentInfo_OK ";
    }

    @Override
    public String payMentInfo_timOut(Integer id) {
        return "服务失败了,你个大傻子: payMentInfo_timOut";
    }
}
