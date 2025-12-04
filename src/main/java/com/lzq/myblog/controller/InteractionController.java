package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.service.InteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "互动功能", description = "点赞相关接口")
@RestController
@RequestMapping("/api/interaction")
@RequiredArgsConstructor
public class InteractionController {
    
    private final InteractionService interactionService;
    
    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() 
                && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }
    
    @Operation(summary = "点赞博文")
    @PostMapping("/like/{postId}")
    public Result<Void> likePost(@PathVariable Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        boolean success = interactionService.likePost(postId, userId);
        return success ? Result.success() : Result.error("已经点赞过了");
    }
    
    @Operation(summary = "取消点赞")
    @DeleteMapping("/like/{postId}")
    public Result<Void> unlikePost(@PathVariable Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        interactionService.unlikePost(postId, userId);
        return Result.success();
    }
    
    @Operation(summary = "获取博文互动状态")
    @GetMapping("/status/{postId}")
    public Result<Map<String, Object>> getInteractionStatus(@PathVariable Long postId) {
        Long userId = getCurrentUserId();
        
        Map<String, Object> status = new HashMap<>();
        status.put("likeCount", interactionService.getLikeCount(postId));
        
        if (userId != null) {
            status.put("isLiked", interactionService.isLiked(postId, userId));
        } else {
            status.put("isLiked", false);
        }
        
        return Result.success(status);
    }
}
