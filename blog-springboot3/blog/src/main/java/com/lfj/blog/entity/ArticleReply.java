package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文章回复表
 * @TableName article_reply
 */
@TableName(value ="article_reply")
@Data
public class ArticleReply implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    @TableField(value = "article_id")
    private Integer articleId;

    /**
     * 评论id
     */
    @TableField(value = "comment_id")
    private Integer commentId;

    /**
     * 评论者id
     */
    @TableField(value = "from_user_id")
    private Integer fromUserId;

    /**
     * 被评论者id
     */
    @TableField(value = "to_user_id")
    private Integer toUserId;

    /**
     * 回复内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 回复时间
     */
    @TableField(value = "reply_time")
    private LocalDateTime replyTime;

    /**
     * 是否已删除，1:是，0:否
     */
    @TableField(value = "deleted")
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}