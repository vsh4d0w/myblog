package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    
    /**
     * 获取博文的所有标签
     */
    @Select("SELECT t.* FROM tag t " +
            "INNER JOIN post_tag pt ON t.id = pt.tag_id " +
            "WHERE pt.post_id = #{postId}")
    List<Tag> selectTagsByPostId(@Param("postId") Long postId);
    
    /**
     * 获取热门标签
     */
    @Select("SELECT * FROM tag ORDER BY use_count DESC LIMIT #{limit}")
    List<Tag> selectHotTags(@Param("limit") Integer limit);
}
