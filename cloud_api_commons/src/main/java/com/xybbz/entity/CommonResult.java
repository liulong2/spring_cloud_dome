package com.xybbz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param <T>
 * @author 刘梦龙22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

//    private Enum code;
    private Integer code;
    private String message;
    private T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
}
