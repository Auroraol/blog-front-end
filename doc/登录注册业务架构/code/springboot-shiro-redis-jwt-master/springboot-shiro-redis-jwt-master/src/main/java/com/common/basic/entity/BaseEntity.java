package com.common.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 创建者 */
    private Long createBy;

    /** 创建时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd ss:mm:ss")
    private Date createTime;

    /** 更新者 */
    private Long updateBy;

    /** 更新时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd ss:mm:ss")
    private Date updateTime;
}
