package com.lzq.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 博文-标签关联表
 */
@Data
@TableName("post_tag")
public class PostTag {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 博文ID
     */
    private Long postId;
    
    /**
     * 标签ID
     */
    private Long tagId;
}
