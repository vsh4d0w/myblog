package com.lzq.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzq.myblog.dto.CommentCreateDTO;
import com.lzq.myblog.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {
    
    /**
     * 创建评论
     * @param createDTO 评论信息
     * @param userId 评论者ID
     * @return 创建的评论
     */
    Comment create(CommentCreateDTO createDTO, Long userId);
    
    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 操作者ID（用于权限校验）
     * @param isAdmin 是否管理员
     * @return 是否成功
     */
    boolean delete(Long commentId, Long userId, boolean isAdmin);
    
    /**
     * 获取博文的所有评论（含回复）
     * @param postId 博文ID
     * @return 评论列表
     */
    List<Comment> getByPostId(Long postId);
    
    /**
     * 获取用户的所有评论
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> getByUserId(Long userId);
    
    /**
     * 更新评论状态（审核）
     * @param commentId 评论ID
     * @param status 状态：1-正常，0-隐藏
     * @return 是否成功
     */
    boolean updateStatus(Long commentId, Integer status);
    
    /**
     * 获取某条评论的所有回复
     * @param parentId 父评论ID
     * @return 回复列表
     */
    List<Comment> getReplies(Long parentId);
}
