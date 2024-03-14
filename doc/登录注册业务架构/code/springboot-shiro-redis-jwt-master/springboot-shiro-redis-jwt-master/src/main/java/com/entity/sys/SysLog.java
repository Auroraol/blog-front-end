package com.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_log")
public class SysLog {

    /**
     * 日志编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作日期
     */
    private String logTime;

    /**
     * 操作账号
     */
    private String logName;

    /**
     * 操作
     */
    private String logMethod;

    /**
     * 主机地址
     */
    private String logIp;

    /**
     * 请求url
     */
    private String logUrl;

    /**
     * 操作状态（0成功 1失败）
     */
    private int status;
}
