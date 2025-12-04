-- =====================================================
-- MyBlog 个人博客系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- 表数量: 4张
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS myblog 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE myblog;

-- =====================================================
-- 删除已存在的表（按外键依赖顺序）
-- =====================================================
DROP TABLE IF EXISTS `post_like`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `blog_post`;
DROP TABLE IF EXISTS `user`;

-- =====================================================
-- 1. 用户表 (user)
-- =====================================================
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（用于登录）',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `role` VARCHAR(20) NOT NULL DEFAULT 'GUEST' COMMENT '角色: ADMIN/GUEST',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 2. 博文表 (blog_post)
-- =====================================================
CREATE TABLE `blog_post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '博文ID',
  `title` VARCHAR(200) NOT NULL COMMENT '标题',
  `content` LONGTEXT NOT NULL COMMENT '内容（Markdown）',
  `summary` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `category` VARCHAR(20) NOT NULL COMMENT '分类: CTF/LEARN/SOMETHING',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-发布 0-草稿',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  FULLTEXT KEY `ft_title_content` (`title`, `content`) WITH PARSER ngram COMMENT '全文搜索索引',
  CONSTRAINT `fk_post_author` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博文表';

-- =====================================================
-- 3. 评论表 (comment)
-- =====================================================
CREATE TABLE `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` BIGINT NOT NULL COMMENT '博文ID',
  `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID（用于回复）',
  `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复目标用户ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-隐藏',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_post` (`post_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `blog_post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- =====================================================
-- 4. 点赞表 (post_like)
-- =====================================================
CREATE TABLE `post_like` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` BIGINT NOT NULL COMMENT '博文ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`, `user_id`),
  KEY `idx_post` (`post_id`),
  KEY `idx_user` (`user_id`),
  CONSTRAINT `fk_like_post` FOREIGN KEY (`post_id`) REFERENCES `blog_post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞表';

-- =====================================================
-- 初始化数据：管理员账号
-- 用户名: sh4d0w
-- 密码: 467194403 (BCrypt加密)
-- =====================================================
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`) VALUES
('sh4d0w', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGM/TEZyj2C2', '博主', 'ADMIN', 1);
