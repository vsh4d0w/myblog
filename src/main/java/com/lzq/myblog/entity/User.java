package com.lzq.myblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 包含博主(ADMIN)和游客(GUEST)两种角色
 */
@Data
@TableName("user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 用户名（自定义ID，唯一） */
    private String username;
    
    /** 密码（加密存储） */
    private String password;
    
    /** 昵称 */
    private String nickname;
    
    /** 邮箱 */
    private String email;
    
    /** 头像URL */
    private String avatar;
    
    /** 角色：ADMIN-博主，GUEST-游客 */
    private String role;
    
    /** 状态：1-正常，0-禁用 */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

