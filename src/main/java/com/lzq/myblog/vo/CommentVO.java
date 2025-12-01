package com.lzq.myblog.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论视图对象（包含用户信息）
 */
@Data
public class CommentVO {
    
    /** 评论ID */
    private Long id;
    
    /** 博文ID */
    private Long postId;
    
    /** 评论内容 */
    private String content;
    
    /** 父评论ID */
    private Long parentId;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    // 用户信息
    /** 评论用户ID */
    private Long userId;
    
    /** 评论者用户名 */
    private String authorName;
    
    /** 评论者昵称 */
    private String authorNickname;
    
    /** 评论者头像 */
    private String authorAvatar;
    
    // 回复信息
    /** 回复给谁的用户ID */
    private Long replyToUserId;
    
    /** 回复给谁的用户名 */
    private String replyToUsername;
}
