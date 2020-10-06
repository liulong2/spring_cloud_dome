package com.xybbz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xybbz.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2020-10-06
 */
 @Mapper
 @Repository
 @Scope("prototype")
public interface PaymentDAO extends BaseMapper<Payment> {

}
