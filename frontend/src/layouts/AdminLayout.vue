<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <el-icon><Setting /></el-icon>
        <span v-if="!isCollapsed">管理后台</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
      >
        <el-menu-item index="/admin">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>仪表盘</template>
        </el-menu-item>
        
        <el-menu-item index="/admin/posts">
          <el-icon><Document /></el-icon>
          <template #title>文章管理</template>
        </el-menu-item>
        
        <el-menu-item index="/admin/comments">
          <el-icon><ChatDotRound /></el-icon>
          <template #title>评论管理</template>
        </el-menu-item>
        
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>
      </el-menu>
    </aside>
    
    <!-- 主区域 -->
    <div class="main-area">
      <!-- 顶部栏 -->
      <header class="header">
        <div class="header-left">
          <el-icon 
            class="collapse-btn" 
            @click="toggleCollapse"
          >
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :icon="UserFilled" />
              <span>{{ userStore.user?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="home">
                  <el-icon><HomeFilled /></el-icon>返回前台
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <!-- 内容区域 -->
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import {
  Setting,
  DataAnalysis,
  Document,
  ChatDotRound,
  User,
  Fold,
  Expand,
  UserFilled,
  HomeFilled,
  SwitchButton
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapsed = ref(false)

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const titles = {
    '/admin': '仪表盘',
    '/admin/posts': '文章管理',
    '/admin/comments': '评论管理',
    '/admin/users': '用户管理'
  }
  return titles[route.path] || '管理后台'
})

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

const handleCommand = async (command) => {
  if (command === 'home') {
    router.push('/')
  } else if (command === 'logout') {
    await userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped lang="scss">
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 220px;
  background-color: #304156;
  transition: width 0.3s;
  
  &.collapsed {
    width: 64px;
    
    .sidebar-header span {
      display: none;
    }
  }
  
  .sidebar-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    color: #fff;
    font-size: 18px;
    font-weight: 600;
    border-bottom: 1px solid #3d4d5f;
    
    .el-icon {
      font-size: 24px;
      color: #409eff;
    }
  }
  
  .el-menu {
    border-right: none;
  }
}

.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
}

.header {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 15px;
    
    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      color: #606266;
      
      &:hover {
        color: #409eff;
      }
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      
      span {
        color: #606266;
        font-size: 14px;
      }
    }
  }
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
