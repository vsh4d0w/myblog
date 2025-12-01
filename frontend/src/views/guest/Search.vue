<template>
  <div class="search-page">
    <h2>搜索文章</h2>
    
    <!-- 搜索框 -->
    <div class="search-bar">
      <el-input
        v-model="keyword"
        size="large"
        placeholder="输入关键词搜索文章..."
        :prefix-icon="Search"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>
    
    <!-- 搜索结果 -->
    <div v-if="searched" class="search-results">
      <p class="result-count">
        找到 <strong>{{ posts.length }}</strong> 篇相关文章
      </p>
      
      <el-skeleton :loading="loading" animated :count="3">
        <template #template>
          <div style="padding: 20px; background: #fff; margin-bottom: 15px; border-radius: 8px;">
            <el-skeleton-item variant="h3" style="width: 50%" />
            <el-skeleton-item variant="text" style="margin-top: 15px" />
          </div>
        </template>
        
        <template #default>
          <div class="post-items">
            <div
              v-for="post in posts"
              :key="post.id"
              class="post-item"
              @click="goToPost(post.id)"
            >
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-summary">{{ post.summary }}</p>
              <div class="post-meta">
                <span class="author">
                  <el-icon><User /></el-icon>
                  {{ post.authorName }}
                </span>
                <span class="date">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(post.createdTime) }}
                </span>
              </div>
            </div>
            
            <el-empty v-if="posts.length === 0" description="没有找到相关文章" />
          </div>
        </template>
      </el-skeleton>
    </div>
    
    <!-- 搜索提示 -->
    <div v-else class="search-tips">
      <el-icon class="tip-icon"><Search /></el-icon>
      <p>输入关键词开始搜索</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { searchPosts } from '@/api/post'
import { Search, User, Calendar } from '@element-plus/icons-vue'

const router = useRouter()

const keyword = ref('')
const posts = ref([])
const loading = ref(false)
const searched = ref(false)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const handleSearch = async () => {
  if (!keyword.value.trim()) return
  
  loading.value = true
  searched.value = true
  
  try {
    const res = await searchPosts(keyword.value)
    if (res.code === 200) {
      posts.value = res.data || []
    }
  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

const goToPost = (id) => {
  router.push(`/post/${id}`)
}
</script>

<style scoped lang="scss">
.search-page {
  max-width: 900px;
  margin: 0 auto;
  
  h2 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 20px;
  }
}

.search-bar {
  margin-bottom: 30px;
  
  .el-input {
    max-width: 600px;
  }
}

.result-count {
  color: #606266;
  margin-bottom: 20px;
  
  strong {
    color: #409eff;
  }
}

.post-items {
  .post-item {
    background: #fff;
    padding: 20px;
    margin-bottom: 15px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    }
    
    .post-title {
      font-size: 18px;
      color: #303133;
      margin: 0 0 10px;
    }
    
    .post-summary {
      color: #606266;
      font-size: 14px;
      line-height: 1.6;
      margin: 0 0 15px;
    }
    
    .post-meta {
      display: flex;
      gap: 20px;
      color: #909399;
      font-size: 13px;
      
      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }
}

.search-tips {
  text-align: center;
  padding: 60px 20px;
  color: #909399;
  
  .tip-icon {
    font-size: 64px;
    margin-bottom: 20px;
  }
  
  p {
    font-size: 16px;
  }
}
</style>
