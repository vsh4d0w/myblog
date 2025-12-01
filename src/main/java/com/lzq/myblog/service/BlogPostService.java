package com.lzq.myblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzq.myblog.dto.PostCreateDTO;
import com.lzq.myblog.dto.PostUpdateDTO;
import com.lzq.myblog.entity.BlogPost;

/**
 * 博文服务接口
 */
public interface BlogPostService extends IService<BlogPost> {
    
    /**
     * 创建博文
     * @param createDTO 博文信息
     * @param authorId 作者ID
     * @return 创建的博文
     */
    BlogPost create(PostCreateDTO createDTO, Long authorId);
    
    /**
     * 更新博文
     * @param updateDTO 更新信息
     * @return 更新后的博文
     */
    BlogPost update(PostUpdateDTO updateDTO);
    
    /**
     * 删除博文
     * @param postId 博文ID
     * @return 是否成功
     */
    boolean delete(Long postId);
    
    /**
     * 根据ID获取博文详情
     * @param postId 博文ID
     * @return 博文信息
     */
    BlogPost getDetail(Long postId);
    
    /**
     * 分页查询博文列表
     * @param page 页码
     * @param size 每页数量
     * @return 分页结果
     */
    IPage<BlogPost> list(int page, int size);
    
    /**
     * 按分类分页查询博文
     * @param category 分类
     * @param page 页码
     * @param size 每页数量
     * @return 分页结果
     */
    IPage<BlogPost> listByCategory(String category, int page, int size);
    
    /**
     * 搜索博文
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 分页结果
     */
    IPage<BlogPost> search(String keyword, int page, int size);
    
    /**
     * 增加浏览量
     * @param postId 博文ID
     */
    void incrementViewCount(Long postId);
}
