package com.lzq.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzq.myblog.dto.UserLoginDTO;
import com.lzq.myblog.dto.UserRegisterDTO;
import com.lzq.myblog.entity.User;
import com.lzq.myblog.exception.BusinessException;
import com.lzq.myblog.mapper.UserMapper;
import com.lzq.myblog.service.UserService;
import com.lzq.myblog.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Override
    public String login(UserLoginDTO loginDTO) {
        User user = baseMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 生成JWT token
        return jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
    }
    
    @Override
    public User register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existUser = baseMapper.selectByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setRole("GUEST");  // 注册用户默认为游客
        user.setStatus(1);
        
        baseMapper.insert(user);
        
        // 返回时清除密码
        user.setPassword(null);
        return user;
    }
    
    @Override
    public User getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }
    
    @Override
    public List<User> getAllGuests() {
        return baseMapper.selectAllGuests();
    }
    
    @Override
    public boolean updateStatus(Long userId, Integer status) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 不能禁用管理员
        if ("ADMIN".equals(user.getRole())) {
            throw new BusinessException("不能禁用管理员账号");
        }
        
        user.setStatus(status);
        return baseMapper.updateById(user) > 0;
    }
    
    @Override
    public boolean deleteUser(Long userId) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 不能删除管理员
        if ("ADMIN".equals(user.getRole())) {
            throw new BusinessException("不能删除管理员账号");
        }
        
        return baseMapper.deleteById(userId) > 0;
    }
}
