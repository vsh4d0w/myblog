package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.entity.User;
import com.lzq.myblog.service.CommentService;
import com.lzq.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器
 */
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
    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        userService.updateStatus(id, status);
        String message = status == 1 ? "用户已启用" : "用户已禁用";
        return Result.success(message, null);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户已删除", null);
    }
    
    /**
     * 审核评论（更新评论状态）
     */
    @PutMapping("/comments/{id}/status")
    public Result<Void> updateCommentStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        commentService.updateStatus(id, status);
        String message = status == 1 ? "评论已显示" : "评论已隐藏";
        return Result.success(message, null);
    }
}
