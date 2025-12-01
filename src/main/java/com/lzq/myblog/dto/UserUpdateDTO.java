package com.lzq.myblog.dto;

import lombok.Data;

/**
 * 用户信息更新DTO
 */
@Data
public class UserUpdateDTO {
    
    /** 昵称 */
    private String nickname;
    
    /** 头像URL */
    private String avatar;
    
    /** 旧密码（修改密码时需要） */
    private String oldPassword;
    
    /** 新密码 */
    private String newPassword;
}
