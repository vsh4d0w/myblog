package com.lzq.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzq.myblog.entity.BlogPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 博文Mapper接口
 */
@Mapper
public interface BlogPostMapper extends BaseMapper<BlogPost> {
    
    /**
     * 根据分类查询已发布的博文
     */
    @Select("SELECT * FROM blog_post WHERE category = #{category} AND status = 1 ORDER BY create_time DESC")
    List<BlogPost> selectByCategory(@Param("category") String category);
    
    /**
     * 搜索博文（标题和内容模糊匹配）
     */
    @Select("SELECT * FROM blog_post WHERE status = 1 AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')) ORDER BY create_time DESC")
    List<BlogPost> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 增加浏览量
     */
    @Update("UPDATE blog_post SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);
    
    /**
     * 获取所有文章的总浏览量
     */
    @Select("SELECT COALESCE(SUM(view_count), 0) FROM blog_post")
    Long getTotalViewCount();
}
