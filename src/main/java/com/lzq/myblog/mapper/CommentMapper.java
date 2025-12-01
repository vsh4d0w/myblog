package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    /**
     * 根据博文ID查询评论（正常状态）
     */
    @Select("SELECT * FROM comment WHERE post_id = #{postId} AND status = 1 ORDER BY create_time ASC")
    List<Comment> selectByPostId(@Param("postId") Long postId);
    
    /**
     * 根据用户ID查询评论
     */
    @Select("SELECT * FROM comment WHERE user_id = #{userId} AND status = 1 ORDER BY create_time DESC")
    List<Comment> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询某评论的所有回复
     */
    @Select("SELECT * FROM comment WHERE parent_id = #{parentId} AND status = 1 ORDER BY create_time ASC")
    List<Comment> selectReplies(@Param("parentId") Long parentId);
}
