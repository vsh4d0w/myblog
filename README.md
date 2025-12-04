# MyBlog 个人博客系统

基于 **Spring Boot 3.2 + Vue 3** 构建的全栈博客系统。

## 技术栈

| 后端 | 前端 |
|------|------|
| Spring Boot 3.2.0 | Vue 3 + Vite |
| Spring Security + JWT | Element Plus |
| MyBatis-Plus 3.5.5 | Vue Router + Pinia |
| MySQL 8.0 | Axios |
| Java 17 | highlight.js |

## 快速启动

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+

### 1. 初始化数据库
```bash
mysql -u root -p < src/main/resources/sql/init.sql
```

### 2. 启动后端 (端口 8080)
```bash
./mvnw spring-boot:run
```

### 3. 启动前端 (端口 5173)
```bash
cd frontend && npm install && npm run dev
```

### 4. 访问地址

| 服务 | 地址 |
|------|------|
| 前端页面 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| API 文档 | http://localhost:8080/doc.html |

### 5. 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | sh4d0w | 467194403 |

## 功能特性

- ✅ 用户认证（登录/注册/JWT）
- ✅ 博文管理（增删改查/分类/搜索）
- ✅ Markdown 编辑器 + 代码高亮
- ✅ 评论系统（发表/回复）
- ✅ 点赞功能
- ✅ 图片上传（支持本地图片自动处理）
- ✅ 管理后台（仪表盘/用户管理/评论审核）
- ✅ Knife4j API 文档

## 项目结构

```
myBlog/
├── src/main/java/com/lzq/myblog/
│   ├── controller/     # 控制器
│   ├── service/        # 业务逻辑
│   ├── mapper/         # 数据访问
│   ├── entity/         # 实体类
│   ├── dto/            # 数据传输对象
│   ├── config/         # 配置类
│   └── filter/         # JWT过滤器
├── src/main/resources/
│   ├── application.properties
│   └── sql/init.sql
└── frontend/           # Vue 3 前端
    ├── src/
    │   ├── views/      # 页面组件
    │   ├── api/        # API 接口
    │   ├── store/      # 状态管理
    │   └── router/     # 路由配置
    └── package.json
```

## License

MIT
