package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.dto.CommentCreateDTO;
import com.lzq.myblog.entity.Comment;
import com.lzq.myblog.service.CommentService;
import com.lzq.myblog.vo.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "评论管理", description = "评论的查询、创建、删除操作")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 获取博文的所有评论（包含用户信息）
     */
    @Operation(summary = "获取博文评论", description = "获取指定博文的所有评论，包含评论者头像和用户名")
    @GetMapping("/post/{postId}")
    public Result<List<CommentVO>> getByPostId(@Parameter(description = "博文ID") @PathVariable Long postId) {
        List<CommentVO> comments = commentService.getByPostIdWithUser(postId);
        return Result.success(comments);
    }
    
    /**
     * 获取某条评论的回复（包含用户信息）
     */
    @Operation(summary = "获取评论回复", description = "获取指定评论的所有回复，包含评论者头像和用户名")
    @GetMapping("/{commentId}/replies")
    public Result<List<CommentVO>> getReplies(@Parameter(description = "评论ID") @PathVariable Long commentId) {
        List<CommentVO> replies = commentService.getRepliesWithUser(commentId);
        return Result.success(replies);
    }
    
    /**
     * 创建评论（需要登录）
     */
    @Operation(summary = "创建评论", description = "发表评论，需要登录")
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
    @Operation(summary = "删除评论", description = "删除评论，评论作者或管理员可操作")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "评论ID") @PathVariable Long id) {
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
