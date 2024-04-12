package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章收藏表
 * @TableName article_collect
 */
@TableName(value ="article_collect")
@Data
public class ArticleCollect implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 文章id
     */
    @TableField(value = "article_id")
    private Integer articleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}