package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.dto.CommentCreateDTO;
import com.lzq.myblog.entity.Comment;
import com.lzq.myblog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 获取博文的所有评论
     */
    @GetMapping("/post/{postId}")
    public Result<List<Comment>> getByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getByPostId(postId);
        return Result.success(comments);
    }
    
    /**
     * 获取某条评论的回复
     */
    @GetMapping("/{commentId}/replies")
    public Result<List<Comment>> getReplies(@PathVariable Long commentId) {
        List<Comment> replies = commentService.getReplies(commentId);
        return Result.success(replies);
    }
    
    /**
     * 创建评论（需要登录）
     */
    @PostMapping
    public Result<Comment> create(@Valid @RequestBody CommentCreateDTO createDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.unauthorized();
        }
        
        Long userId = (Long) authentication.getPrincipal();
        Comment comment = commentService.create(createDTO, userId);
        return Result.success("评论成功", comment);
    }
    
    /**
     * 删除评论（评论作者或管理员可删除）
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.unauthorized();
        }
        
        Long userId = (Long) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        
        commentService.delete(id, userId, isAdmin);
        return Result.success("删除成功", null);
    }
}
