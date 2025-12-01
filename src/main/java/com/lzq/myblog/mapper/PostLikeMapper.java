package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
    
    /**
     * 检查用户是否点赞过博文
     */
    @Select("SELECT COUNT(*) FROM post_like WHERE post_id = #{postId} AND user_id = #{userId}")
    int checkLiked(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 获取博文点赞数
     */
    @Select("SELECT COUNT(*) FROM post_like WHERE post_id = #{postId}")
    int countByPostId(@Param("postId") Long postId);
}
