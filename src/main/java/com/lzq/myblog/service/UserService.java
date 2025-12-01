package com.lzq.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzq.myblog.dto.UserLoginDTO;
import com.lzq.myblog.dto.UserRegisterDTO;
import com.lzq.myblog.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录成功返回JWT token
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 用户注册（仅限游客注册）
     * @param registerDTO 注册信息
     * @return 注册成功的用户
     */
    User register(UserRegisterDTO registerDTO);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);
    
    /**
     * 获取所有游客用户
     * @return 游客列表
     */
    List<User> getAllGuests();
    
    /**
     * 更新用户状态（启用/禁用）
     * @param userId 用户ID
     * @param status 状态：1-正常，0-禁用
     * @return 是否成功
     */
    boolean updateStatus(Long userId, Integer status);
    
    /**
     * 删除用户（仅管理员可操作）
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long userId);
}
