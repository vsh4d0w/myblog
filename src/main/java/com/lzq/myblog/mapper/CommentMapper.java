package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.Comment;
import com.lzq.myblog.vo.CommentVO;
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
     * 根据博文ID查询评论（包含用户信息）
     */
    @Select("SELECT c.id, c.post_id, c.user_id, c.content, c.parent_id, c.reply_to_user_id, c.create_time, " +
            "u.username AS author_name, u.nickname AS author_nickname, u.avatar AS author_avatar, " +
            "ru.username AS reply_to_username " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN user ru ON c.reply_to_user_id = ru.id " +
            "WHERE c.post_id = #{postId} AND c.status = 1 " +
            "ORDER BY c.create_time ASC")
    List<CommentVO> selectByPostIdWithUser(@Param("postId") Long postId);
    
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
    
    /**
     * 查询某评论的所有回复（包含用户信息）
     */
    @Select("SELECT c.id, c.post_id, c.user_id, c.content, c.parent_id, c.reply_to_user_id, c.create_time, " +
            "u.username AS author_name, u.nickname AS author_nickname, u.avatar AS author_avatar, " +
            "ru.username AS reply_to_username " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN user ru ON c.reply_to_user_id = ru.id " +
            "WHERE c.parent_id = #{parentId} AND c.status = 1 " +
            "ORDER BY c.create_time ASC")
    List<CommentVO> selectRepliesWithUser(@Param("parentId") Long parentId);
}
