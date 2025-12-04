package com.lzq.myblog.service;

public interface InteractionService {
    
    /**
     * 点赞博文
     */
    boolean likePost(Long postId, Long userId);
    
    /**
     * 取消点赞
     */
    boolean unlikePost(Long postId, Long userId);
    
    /**
     * 检查是否点赞
     */
    boolean isLiked(Long postId, Long userId);
    
    /**
     * 获取博文点赞数
     */
    int getLikeCount(Long postId);
}
