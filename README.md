# 个人博客系统 (MyBlog)

基于 Spring Boot 3.2 + Vue 3 的个人博客系统。

## 技术栈

### 后端
- Spring Boot 3.2.0
- Spring Security + JWT
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Java 17

### 前端
- Vue 3
- Vite
- Vue Router 4
- Pinia
- Element Plus
- Axios

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+

### 1. 数据库初始化

```bash
mysql -u root -p < src/main/resources/sql/init.sql
```

### 2. 启动后端服务（端口 8080）

```bash
# 在项目根目录下执行
./mvnw spring-boot:run
```

### 3. 启动前端服务（端口 5173）

```bash
cd frontend
npm install  # 首次运行需要安装依赖
npm run dev
```

## 访问地址

| 服务 | 地址 |
|------|------|
| 前端页面 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| API 文档 | http://localhost:8080/doc.html |

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | sh4d0w | 467194403 |

## 主要功能

- ✅ 用户认证（登录/注册）
- ✅ 博文管理（增删改查）
- ✅ 分类管理
- ✅ 标签管理
- ✅ 评论系统
- ✅ 点赞/收藏
- ✅ Markdown 编辑器
- ✅ 文件上传
- ✅ 管理后台

## 项目结构

```
myBlog/
├── src/main/java/          # 后端 Java 代码
│   └── com/lzq/myblog/
│       ├── controller/     # 控制器层
│       ├── service/        # 服务层
│       ├── mapper/         # 数据访问层
│       ├── entity/         # 实体类
│       ├── dto/            # 数据传输对象
│       ├── config/         # 配置类
│       ├── filter/         # 过滤器
│       └── utils/          # 工具类
├── src/main/resources/     # 后端资源文件
│   ├── application.properties
│   └── sql/init.sql
└── frontend/               # 前端 Vue 项目
    ├── src/
    │   ├── views/          # 页面组件
    │   ├── components/     # 公共组件
    │   ├── api/            # API 接口
    │   ├── store/          # 状态管理
    │   └── router/         # 路由配置
    └── package.json
```

## License

MIT
