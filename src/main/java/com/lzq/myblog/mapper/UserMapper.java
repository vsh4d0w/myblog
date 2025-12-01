package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);
    
    /**
     * 查询所有游客账号
     */
    @Select("SELECT * FROM user WHERE role = 'GUEST' ORDER BY create_time DESC")
    List<User> selectAllGuests();
    
    /**
     * 根据角色查询用户数量
     */
    @Select("SELECT COUNT(*) FROM user WHERE role = #{role}")
    int countByRole(@Param("role") String role);
}
