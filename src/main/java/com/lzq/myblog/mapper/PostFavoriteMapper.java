package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.PostFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostFavoriteMapper extends BaseMapper<PostFavorite> {
    
    /**
     * 检查用户是否收藏过博文
     */
    @Select("SELECT COUNT(*) FROM post_favorite WHERE post_id = #{postId} AND user_id = #{userId}")
    int checkFavorited(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 获取博文收藏数
     */
    @Select("SELECT COUNT(*) FROM post_favorite WHERE post_id = #{postId}")
    int countByPostId(@Param("postId") Long postId);
}
