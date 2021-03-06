package com.xybbz.entity;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liu
 * @since 2020-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@TableName("payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

//    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String serial;


}
