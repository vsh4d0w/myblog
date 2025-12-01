package com.lzq.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzq.myblog.dto.CommentCreateDTO;
import com.lzq.myblog.entity.Comment;
import com.lzq.myblog.exception.BusinessException;
import com.lzq.myblog.mapper.CommentMapper;
import com.lzq.myblog.service.CommentService;
import com.lzq.myblog.vo.CommentVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务实现
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    
    @Override
    public Comment create(CommentCreateDTO createDTO, Long userId) {
        Comment comment = new Comment();
        comment.setPostId(createDTO.getPostId());
        comment.setUserId(userId);
        comment.setContent(createDTO.getContent());
        comment.setParentId(createDTO.getParentId());
        comment.setReplyToUserId(createDTO.getReplyToUserId());
        comment.setStatus(1);  // 默认正常状态
        
        baseMapper.insert(comment);
        return comment;
    }
    
    @Override
    public boolean delete(Long commentId, Long userId, boolean isAdmin) {
        Comment comment = baseMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        // 只有评论作者或管理员可以删除
        if (!isAdmin && !comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此评论");
        }
        
        return baseMapper.deleteById(commentId) > 0;
    }
    
    @Override
    public List<Comment> getByPostId(Long postId) {
        return baseMapper.selectByPostId(postId);
    }
    
    @Override
    public List<CommentVO> getByPostIdWithUser(Long postId) {
        return baseMapper.selectByPostIdWithUser(postId);
    }
    
    @Override
    public List<Comment> getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
    
    @Override
    public boolean updateStatus(Long commentId, Integer status) {
        Comment comment = baseMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        comment.setStatus(status);
        return baseMapper.updateById(comment) > 0;
    }
    
    @Override
    public List<Comment> getReplies(Long parentId) {
        return baseMapper.selectReplies(parentId);
    }
    
    @Override
    public List<CommentVO> getRepliesWithUser(Long parentId) {
        return baseMapper.selectRepliesWithUser(parentId);
    }
}
