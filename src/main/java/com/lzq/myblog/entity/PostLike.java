package com.lzq.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 点赞实体类
 */
@Data
@TableName("post_like")
public class PostLike {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 博文ID
     */
    private Long postId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
