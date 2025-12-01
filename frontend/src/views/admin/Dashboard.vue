<template>
  <div class="admin-dashboard">
    <h2>仪表盘</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.postCount }}</p>
            <p class="stat-label">文章总数</p>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #67c23a">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.commentCount }}</p>
            <p class="stat-label">评论总数</p>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.userCount }}</p>
            <p class="stat-label">用户总数</p>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <el-icon><View /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ stats.viewCount }}</p>
            <p class="stat-label">总浏览量</p>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <!-- 快捷操作 -->
    <div class="quick-actions">
      <h3>快捷操作</h3>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" size="large" @click="$router.push('/admin/posts')">
            <el-icon><Edit /></el-icon>
            管理文章
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="success" size="large" @click="$router.push('/admin/comments')">
            <el-icon><ChatDotRound /></el-icon>
            管理评论
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="warning" size="large" @click="$router.push('/admin/users')">
            <el-icon><User /></el-icon>
            管理用户
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="info" size="large" @click="$router.push('/')">
            <el-icon><HomeFilled /></el-icon>
            返回前台
          </el-button>
        </el-col>
      </el-row>
    </div>
    
    <!-- 欢迎信息 -->
    <div class="welcome-card">
      <h3>欢迎回来，{{ userStore.user?.username }}!</h3>
      <p>当前登录时间：{{ currentTime }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { Document, ChatDotRound, User, View, Edit, HomeFilled } from '@element-plus/icons-vue'
import { getPosts } from '@/api/post'
import { getAllUsers, getAllComments } from '@/api/admin'

const userStore = useUserStore()

const stats = ref({
  postCount: 0,
  commentCount: 0,
  userCount: 0,
  viewCount: 0
})

const currentTime = ref(new Date().toLocaleString('zh-CN'))

const fetchStats = async () => {
  try {
    // 获取文章统计
    const postRes = await getPosts({ page: 1, size: 1 })
    if (postRes.code === 200) {
      stats.value.postCount = postRes.data?.total || 0
    }
    
    // 获取用户统计
    const userRes = await getAllUsers({ page: 1, size: 1 })
    if (userRes.code === 200) {
      stats.value.userCount = userRes.data?.total || 0
    }
    
    // 获取评论统计
    const commentRes = await getAllComments({ page: 1, size: 1 })
    if (commentRes.code === 200) {
      stats.value.commentCount = commentRes.data?.total || 0
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped lang="scss">
.admin-dashboard {
  h2 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 20px;
  }
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  
  .stat-icon {
    width: 50px;
    height: 50px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .el-icon {
      font-size: 24px;
      color: #fff;
    }
  }
  
  .stat-info {
    .stat-value {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
      margin: 5px 0 0;
    }
  }
}

.quick-actions {
  margin-top: 30px;
  
  h3 {
    font-size: 18px;
    color: #303133;
    margin-bottom: 15px;
  }
  
  .el-button {
    width: 100%;
    height: 60px;
    font-size: 15px;
    
    .el-icon {
      margin-right: 8px;
    }
  }
}

.welcome-card {
  margin-top: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 30px;
  color: #fff;
  
  h3 {
    font-size: 24px;
    margin: 0 0 10px;
  }
  
  p {
    margin: 0;
    opacity: 0.8;
  }
}
</style>
