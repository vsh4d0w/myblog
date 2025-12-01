package com.lzq.myblog.controller;

import com.lzq.myblog.common.Result;
import com.lzq.myblog.dto.SendCodeDTO;
import com.lzq.myblog.dto.UserLoginDTO;
import com.lzq.myblog.dto.UserRegisterDTO;
import com.lzq.myblog.entity.User;
import com.lzq.myblog.service.EmailService;
import com.lzq.myblog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户登录、注册、邮箱验证、获取当前用户信息")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final EmailService emailService;
    
    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "通过用户名和密码登录，返回 JWT Token")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        User user = userService.getByUsername(loginDTO.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", buildUserInfo(user));
        
        return Result.success("登录成功", data);
    }
    
    /**
     * 发送邮箱验证码
     */
    @Operation(summary = "发送验证码", description = "向指定邮箱发送注册验证码，有效期5分钟")
    @PostMapping("/send-code")
    public Result<Void> sendVerificationCode(@Valid @RequestBody SendCodeDTO sendCodeDTO) {
        boolean success = emailService.sendVerificationCode(sendCodeDTO.getEmail());
        if (success) {
            return Result.success("验证码已发送，请查收邮件", null);
        } else {
            return Result.error("验证码发送失败，请稍后重试");
        }
    }
    
    /**
     * 用户注册（游客注册）
     */
    @Operation(summary = "用户注册", description = "游客注册账号，需要邮箱验证码（如配置了邮箱服务）")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        // 如果提供了邮箱和验证码，进行验证
        if (registerDTO.getEmail() != null && !registerDTO.getEmail().isEmpty()) {
            if (registerDTO.getEmailCode() == null || registerDTO.getEmailCode().isEmpty()) {
                return Result.error("请输入邮箱验证码");
            }
            if (!emailService.verifyCode(registerDTO.getEmail(), registerDTO.getEmailCode())) {
                return Result.error("邮箱验证码错误或已过期");
            }
        }
        
        User user = userService.register(registerDTO);
        return Result.success("注册成功", user);
    }
    
    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前用户", description = "获取当前登录用户的信息，需要 JWT 认证")
    @GetMapping("/me")
    public Result<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.unauthorized();
        }
        
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.getById(userId);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        return Result.success(buildUserInfo(user));
    }
    
    /**
     * 构建用户信息（不包含敏感信息）
     */
    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("email", user.getEmail());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("role", user.getRole());
        return userInfo;
    }
}
