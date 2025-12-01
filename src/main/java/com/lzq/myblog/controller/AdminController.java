package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.entity.User;
import com.lzq.myblog.service.CommentService;
import com.lzq.myblog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器
 */
@Tag(name = "管理员管理", description = "管理员专用接口，需要 ADMIN 角色")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    
    private final UserService userService;
    private final CommentService commentService;
    
    /**
     * 获取所有游客用户
     */
    @Operation(summary = "获取所有游客", description = "获取所有角色为 GUEST 的用户列表")
    @GetMapping("/users")
    public Result<List<User>> getAllGuests() {
        List<User> guests = userService.getAllGuests();
        // 清除密码信息
        guests.forEach(user -> user.setPassword(null));
        return Result.success(guests);
    }
    
    /**
     * 更新用户状态（启用/禁用）
     */
    @Operation(summary = "更新用户状态", description = "启用或禁用用户账号")
    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态: 1-启用, 0-禁用") @RequestParam Integer status) {
        userService.updateStatus(id, status);
        String message = status == 1 ? "用户已启用" : "用户已禁用";
        return Result.success(message, null);
    }
    
    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "删除指定用户，不能删除管理员")
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户已删除", null);
    }
    
    /**
     * 审核评论（更新评论状态）
     */
    @Operation(summary = "审核评论", description = "显示或隐藏评论")
    @PutMapping("/comments/{id}/status")
    public Result<Void> updateCommentStatus(
            @Parameter(description = "评论ID") @PathVariable Long id,
            @Parameter(description = "状态: 1-显示, 0-隐藏") @RequestParam Integer status) {
        commentService.updateStatus(id, status);
        String message = status == 1 ? "评论已显示" : "评论已隐藏";
        return Result.success(message, null);
    }
}
