-- =====================================================
-- 个人博客系统数据库初始化脚本
-- 创建时间: 2025-12-01
-- 博主账号: sh4d0w
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS myblog 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE myblog;

-- =====================================================
-- 1. 用户表 (user)
-- =====================================================
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `blog_post`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名（自定义ID，用于登录）',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密存储）',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `role` VARCHAR(20) NOT NULL DEFAULT 'GUEST' COMMENT '角色：ADMIN（博主）/GUEST（游客）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
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
  `content` LONGTEXT NOT NULL COMMENT '内容',
  `summary` VARCHAR(500) COMMENT '摘要',
  `category` VARCHAR(20) NOT NULL COMMENT '分类：CTF/Learn/Something',
  `cover_image` VARCHAR(255) COMMENT '封面图片URL',
  `author_id` BIGINT NOT NULL COMMENT '作者ID（博主）',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-发布 0-草稿',
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
  `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复给谁（用户ID）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-删除',
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
-- 4. 插入博主账号 (密码使用BCrypt加密后的值)
-- 原始密码: 467194403
-- BCrypt加密后: $2b$10$o4w5gV8d8Yxn5..0taA7K.qtzrbqBBYUWqiiIDLzDFXyrwiUzoq2y
-- =====================================================
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`, `create_time`, `update_time`)
VALUES ('sh4d0w', '$2b$10$o4w5gV8d8Yxn5..0taA7K.qtzrbqBBYUWqiiIDLzDFXyrwiUzoq2y', '博主', 'ADMIN', 1, NOW(), NOW());

-- =====================================================
-- 5. 插入测试博文数据（可选）
-- =====================================================
INSERT INTO `blog_post` (`title`, `content`, `summary`, `category`, `author_id`, `view_count`, `status`)
VALUES 
('欢迎来到我的博客', '# 欢迎\n\n这是我的第一篇博文，欢迎大家来访！\n\n## 关于我\n\n我是sh4d0w，一名网络安全爱好者。', '这是我的第一篇博文，欢迎大家来访！', 'Something', 1, 0, 1),
('CTF入门指南', '# CTF入门指南\n\n## 什么是CTF？\n\nCTF（Capture The Flag）是一种网络安全竞赛...', 'CTF入门指南，带你了解网络安全竞赛', 'CTF', 1, 0, 1),
('Java学习笔记', '# Java学习笔记\n\n## 基础语法\n\nJava是一门面向对象的编程语言...', 'Java基础学习笔记', 'Learn', 1, 0, 1);

-- =====================================================
-- 完成提示
-- =====================================================
SELECT '数据库初始化完成！' AS message;
SELECT '博主账号: sh4d0w' AS admin_info;
SELECT '请使用密码: 467194403 登录' AS password_info;
