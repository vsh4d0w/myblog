package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.entity.User;
import com.lzq.myblog.service.InteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "互动功能", description = "点赞/收藏相关接口")
@RestController
@RequestMapping("/api/interaction")
@RequiredArgsConstructor
public class InteractionController {
    
    private final InteractionService interactionService;
    
    @Operation(summary = "点赞博文")
    @PostMapping("/like/{postId}")
    public Result<Void> likePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        boolean success = interactionService.likePost(postId, user.getId());
        return success ? Result.success() : Result.error("已经点赞过了");
    }
    
    @Operation(summary = "取消点赞")
    @DeleteMapping("/like/{postId}")
    public Result<Void> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        interactionService.unlikePost(postId, user.getId());
        return Result.success();
    }
    
    @Operation(summary = "收藏博文")
    @PostMapping("/favorite/{postId}")
    public Result<Void> favoritePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        boolean success = interactionService.favoritePost(postId, user.getId());
        return success ? Result.success() : Result.error("已经收藏过了");
    }
    
    @Operation(summary = "取消收藏")
    @DeleteMapping("/favorite/{postId}")
    public Result<Void> unfavoritePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        interactionService.unfavoritePost(postId, user.getId());
        return Result.success();
    }
    
    @Operation(summary = "获取博文互动状态")
    @GetMapping("/status/{postId}")
    public Result<Map<String, Object>> getInteractionStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        Map<String, Object> status = new HashMap<>();
        status.put("likeCount", interactionService.getLikeCount(postId));
        status.put("favoriteCount", interactionService.getFavoriteCount(postId));
        
        if (user != null) {
            status.put("isLiked", interactionService.isLiked(postId, user.getId()));
            status.put("isFavorited", interactionService.isFavorited(postId, user.getId()));
        } else {
            status.put("isLiked", false);
            status.put("isFavorited", false);
        }
        
        return Result.success(status);
    }
}
