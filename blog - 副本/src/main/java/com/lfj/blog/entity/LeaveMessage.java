package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 留言表
 * @TableName leave_message
 */
@TableName(value ="leave_message")
@Data
public class LeaveMessage implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父id
     */
    @TableField(value = "pid")
    private Integer pid;

    /**
     * 留言者id
     */
    @TableField(value = "from_user_id")
    private Integer fromUserId;

    /**
     * 
     */
    @TableField(value = "to_user_id")
    private Integer toUserId;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否删除，1：是，0：否
     */
    @TableField(value = "deleted")
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}