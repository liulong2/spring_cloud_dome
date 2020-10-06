package com.xybbz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xybbz.entity.Payment;
import com.xybbz.mapper.PaymentDAO;
import com.xybbz.service.PaymentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2020-10-06
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentDAO, Payment> implements PaymentService {


}
