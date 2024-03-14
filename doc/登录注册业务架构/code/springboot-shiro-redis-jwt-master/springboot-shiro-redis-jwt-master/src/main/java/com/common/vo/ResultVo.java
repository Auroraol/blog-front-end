package com.common.vo;

import lombok.Data;


/**
 * 固定返回格式
 */
@Data
public class ResultVo {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 具体的内容
     */
    private Object data;

}
