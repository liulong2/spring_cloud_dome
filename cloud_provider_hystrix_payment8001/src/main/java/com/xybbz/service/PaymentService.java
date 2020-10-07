package com.xybbz.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    /**
     * 正常访问方法
     * @param id
     * @return
     */
    public String payMentInfo_OK(Integer id) {
        return "线程池: " + Thread.currentThread().getName() + " payMentInfo_OK" + id + "哈哈哈";
    }

    public String payMentInfo_timOut(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池: " + Thread.currentThread().getName() + " payMentInfo_timOut" + id + "哈哈哈" + "我是你爸";
    }

}
