<template>
  <div class="home">
    <!-- 欢迎横幅 -->
    <section class="hero">
      <h1>欢迎来到我的博客</h1>
      <p>分享技术、记录生活</p>
    </section>
    
    <!-- 最新文章 -->
    <section class="latest-posts">
      <div class="section-header">
        <h2>最新文章</h2>
        <router-link to="/posts" class="view-all">查看全部 →</router-link>
      </div>
      
      <el-skeleton :loading="loading" animated :count="3">
        <template #template>
          <el-row :gutter="20">
            <el-col :span="8" v-for="i in 3" :key="i">
              <div class="skeleton-card">
                <el-skeleton-item variant="image" style="height: 150px" />
                <div style="padding: 14px">
                  <el-skeleton-item variant="h3" style="width: 80%" />
                  <el-skeleton-item variant="text" style="margin-top: 10px" />
                  <el-skeleton-item variant="text" style="width: 60%" />
                </div>
              </div>
            </el-col>
          </el-row>
        </template>
        
        <template #default>
          <el-row :gutter="20">
            <el-col
              v-for="post in posts"
              :key="post.id"
              :xs="24"
              :sm="12"
              :md="8"
            >
              <div class="post-card" @click="goToPost(post.id)">
                <div class="post-cover">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="post-info">
                  <h3 class="post-title">{{ post.title }}</h3>
                  <p class="post-summary">{{ post.summary || '暂无摘要' }}</p>
                  <div class="post-meta">
                    <span>{{ post.authorName }}</span>
                    <span>{{ formatDate(post.createdTime) }}</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
          
          <el-empty v-if="posts.length === 0" description="暂无文章" />
        </template>
      </el-skeleton>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPosts } from '@/api/post'
import { Document } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(true)
const posts = ref([])

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await getPosts({ page: 1, size: 6 })
    if (res.code === 200) {
      posts.value = res.data?.records || res.data || []
    }
  } catch (error) {
    console.error('获取文章失败:', error)
  } finally {
    loading.value = false
  }
}

const goToPost = (id) => {
  router.push(`/post/${id}`)
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped lang="scss">
.home {
  max-width: 1100px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  margin-bottom: 40px;
  color: #fff;
  
  h1 {
    font-size: 36px;
    margin: 0 0 10px;
  }
  
  p {
    font-size: 18px;
    margin: 0;
    opacity: 0.9;
  }
}

.latest-posts {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h2 {
      font-size: 24px;
      color: #303133;
      margin: 0;
    }
    
    .view-all {
      color: #409eff;
      text-decoration: none;
      font-size: 14px;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.skeleton-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.post-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }
  
  .post-cover {
    height: 150px;
    background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    
    .el-icon {
      font-size: 48px;
      color: #fff;
    }
  }
  
  .post-info {
    padding: 15px;
    
    .post-title {
      font-size: 16px;
      color: #303133;
      margin: 0 0 10px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    .post-summary {
      font-size: 13px;
      color: #909399;
      margin: 0 0 10px;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      min-height: 40px;
    }
    
    .post-meta {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: #909399;
    }
  }
}
</style>
