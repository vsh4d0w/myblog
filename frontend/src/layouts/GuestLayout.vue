<template>
  <div class="guest-layout">
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <div class="navbar-container">
        <div class="logo" @click="goHome">
          <el-icon><Edit /></el-icon>
          <span>我的博客</span>
        </div>
        
        <nav class="nav-menu">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/posts" class="nav-item">文章</router-link>
          <router-link to="/search" class="nav-item">搜索</router-link>
        </nav>
        
        <div class="nav-right">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <el-avatar :size="32" :icon="UserFilled" />
                <span class="username">{{ userStore.user?.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="userStore.isAdmin" command="admin">
                    <el-icon><Setting /></el-icon>管理后台
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" link @click="goLogin">登录</el-button>
            <el-button type="primary" @click="goRegister">注册</el-button>
          </template>
        </div>
      </div>
    </header>
    
    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>
    
    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>© 2024 我的博客 - 使用 Vue 3 + Spring Boot 构建</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { Edit, UserFilled, Setting, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const goHome = () => {
  router.push('/')
}

const goLogin = () => {
  router.push('/login')
}

const goRegister = () => {
  router.push('/register')
}

const handleCommand = async (command) => {
  if (command === 'admin') {
    router.push('/admin')
  } else if (command === 'logout') {
    await userStore.logout()
    router.push('/')
  }
}
</script>

<style scoped lang="scss">
.guest-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  
  .navbar-container {
    max-width: 1200px;
    margin: 0 auto;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
  }
  
  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    font-size: 20px;
    font-weight: 600;
    color: #409eff;
    
    .el-icon {
      font-size: 24px;
    }
  }
  
  .nav-menu {
    display: flex;
    gap: 30px;
    
    .nav-item {
      color: #606266;
      text-decoration: none;
      font-size: 15px;
      transition: color 0.3s;
      
      &:hover,
      &.router-link-active {
        color: #409eff;
      }
    }
  }
  
  .nav-right {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .user-dropdown {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      
      .username {
        color: #606266;
        font-size: 14px;
      }
    }
  }
}

.main-content {
  flex: 1;
  margin-top: 60px;
  padding: 20px;
  max-width: 1200px;
  width: 100%;
  margin-left: auto;
  margin-right: auto;
  box-sizing: border-box;
}

.footer {
  background: #fff;
  padding: 20px;
  text-align: center;
  border-top: 1px solid #ebeef5;
  
  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    
    p {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
}
</style>
