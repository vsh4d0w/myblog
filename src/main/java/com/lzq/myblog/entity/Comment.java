package com.lzq.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@TableName("comment")
public class Comment {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 博文ID */
    private Long postId;
    
    /** 评论用户ID */
    private Long userId;
    
    /** 评论内容 */
    private String content;
    
    /** 父评论ID（用于回复） */
    private Long parentId;
    
    /** 回复给谁（用户ID） */
    private Long replyToUserId;
    
    /** 状态：1-正常，0-删除 */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

