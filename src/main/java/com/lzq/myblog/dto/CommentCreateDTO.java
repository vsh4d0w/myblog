package com.lzq.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建评论请求DTO
 */
@Data
public class CommentCreateDTO {
    
    @NotNull(message = "博文ID不能为空")
    private Long postId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    /** 父评论ID（回复评论时使用） */
    private Long parentId;
    
    /** 回复给谁（用户ID，回复评论时使用） */
    private Long replyToUserId;
}
