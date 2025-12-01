package com.lzq.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 */
@Data
@TableName("post_favorite")
public class PostFavorite {
    
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
