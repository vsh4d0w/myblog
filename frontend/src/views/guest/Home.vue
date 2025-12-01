<template>
  <div class="home">
    <!-- Hero Section with Background -->
    <section class="hero">
      <div class="hero-bg">
        <div class="gradient-overlay"></div>
        <div class="floating-shapes">
          <div class="shape shape-1"></div>
          <div class="shape shape-2"></div>
          <div class="shape shape-3"></div>
        </div>
      </div>
      <div class="hero-content">
        <div class="hero-badge">🚀 Welcome to my blog</div>
        <h1 class="hero-title">sh4d0w's Blog</h1>
        <p class="hero-subtitle">探索 · 学习 · 分享</p>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-value">{{ stats.postCount }}</span>
            <span class="stat-label">文章</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.viewCount }}</span>
            <span class="stat-label">浏览</span>
          </div>
        </div>
      </div>
      <div class="scroll-indicator" @click="scrollToContent">
        <span>向下浏览</span>
        <el-icon class="arrow-icon"><ArrowDown /></el-icon>
      </div>
    </section>

    <!-- Main Content -->
    <section ref="contentRef" class="main-section">
      <div class="content-wrapper">
        <!-- 左侧文章列表 -->
        <div class="posts-section">
          <div class="section-header">
            <h2>最新文章</h2>
            <router-link to="/posts" class="view-all">
              查看全部 <el-icon><ArrowRight /></el-icon>
            </router-link>
          </div>
          
          <el-skeleton :loading="loading" animated :count="3">
            <template #template>
              <div class="post-card skeleton">
                <el-skeleton-item variant="image" style="width: 200px; height: 150px" />
                <div style="flex: 1; padding: 15px">
                  <el-skeleton-item variant="h3" style="width: 60%" />
                  <el-skeleton-item variant="text" style="margin-top: 10px" />
                  <el-skeleton-item variant="text" style="width: 80%" />
                </div>
              </div>
            </template>
            
            <template #default>
              <div 
                v-for="post in posts" 
                :key="post.id" 
                class="post-card"
                @click="goToPost(post.id)"
              >
                <div class="post-cover" v-if="post.coverImage">
                  <img :src="post.coverImage" :alt="post.title" />
                </div>
                <div class="post-cover default-cover" v-else>
                  <el-icon><Document /></el-icon>
                </div>
                <div class="post-info">
                  <div class="post-category">
                    <el-tag size="small" :type="getCategoryType(post.category)">
                      {{ post.category }}
                    </el-tag>
                  </div>
                  <h3 class="post-title">{{ post.title }}</h3>
                  <p class="post-summary">{{ post.summary }}</p>
                  <div class="post-meta">
                    <span><el-icon><Calendar /></el-icon> {{ formatDate(post.createTime) }}</span>
                    <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
                  </div>
                </div>
              </div>
              
              <el-empty v-if="posts.length === 0" description="暂无文章" />
            </template>
          </el-skeleton>
        </div>
        
        <!-- 右侧边栏 -->
        <aside class="sidebar">
          <!-- 分类 -->
          <div class="sidebar-card">
            <h3 class="sidebar-title">
              <el-icon><Folder /></el-icon> 分类
            </h3>
            <ul class="category-list">
              <li @click="filterByCategory('CTF')">
                <span>CTF</span>
                <el-icon><ArrowRight /></el-icon>
              </li>
              <li @click="filterByCategory('Learn')">
                <span>学习笔记</span>
                <el-icon><ArrowRight /></el-icon>
              </li>
              <li @click="filterByCategory('Something')">
                <span>日常随笔</span>
                <el-icon><ArrowRight /></el-icon>
              </li>
            </ul>
          </div>
          
          <!-- 关于博主 -->
          <div class="sidebar-card about-card">
            <div class="about-avatar">
              <img src="@/assets/images/admin-avatar.jpg" alt="sh4d0w" class="avatar-img" />
            </div>
            <h4 class="about-name">sh4d0w</h4>
            <p class="about-bio">网络安全爱好者 | 全栈开发者</p>
            <div class="about-links">
              <el-button circle :icon="Link" />
            </div>
          </div>
        </aside>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPosts } from '@/api/post'
import { 
  ArrowDown, ArrowRight, Document, Calendar, View, 
  Folder, UserFilled, Link 
} from '@element-plus/icons-vue'

const router = useRouter()

const loading = ref(true)
const posts = ref([])
const contentRef = ref(null)

const stats = ref({
  postCount: 0,
  viewCount: 0
})

const getCategoryType = (category) => {
  const types = {
    'CTF': 'danger',
    'Learn': 'success',
    'Something': 'info'
  }
  return types[category] || 'info'
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

const scrollToContent = () => {
  contentRef.value?.scrollIntoView({ behavior: 'smooth' })
}

const goToPost = (id) => {
  router.push(`/post/${id}`)
}

const filterByCategory = (category) => {
  router.push({ path: '/posts', query: { category } })
}

const fetchData = async () => {
  loading.value = true
  try {
    // 获取文章
    const postRes = await getPosts({ page: 1, size: 6 })
    if (postRes.code === 200) {
      posts.value = postRes.data?.records || postRes.data || []
      stats.value.postCount = postRes.data?.total || posts.value.length
      stats.value.viewCount = posts.value.reduce((sum, p) => sum + (p.viewCount || 0), 0)
    }
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.home {
  min-height: 100vh;
  background: #f8fafc;
}

// Hero Section - 全新简洁设计
.hero {
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  
  .hero-bg {
    position: absolute;
    inset: 0;
    background: url('@/assets/images/blog-bg.jpg') center/cover no-repeat;
    
    .gradient-overlay {
      position: absolute;
      inset: 0;
      background: linear-gradient(
        135deg, 
        rgba(102, 126, 234, 0.15) 0%, 
        rgba(118, 75, 162, 0.15) 100%
      );
    }
    
    .floating-shapes {
      position: absolute;
      inset: 0;
      
      .shape {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.05);
        backdrop-filter: blur(5px);
      }
      
      .shape-1 {
        width: 300px;
        height: 300px;
        top: -50px;
        right: -50px;
        animation: float1 15s ease-in-out infinite;
      }
      
      .shape-2 {
        width: 200px;
        height: 200px;
        bottom: 10%;
        left: 5%;
        animation: float2 12s ease-in-out infinite;
      }
      
      .shape-3 {
        width: 150px;
        height: 150px;
        top: 40%;
        left: 20%;
        animation: float3 10s ease-in-out infinite;
      }
    }
  }
  
  .hero-content {
    position: relative;
    z-index: 1;
    text-align: center;
    color: #fff;
    padding: 20px;
  }
  
  .hero-badge {
    display: inline-block;
    padding: 8px 20px;
    background: rgba(255, 255, 255, 0.15);
    backdrop-filter: blur(10px);
    border-radius: 50px;
    font-size: 14px;
    margin-bottom: 24px;
    animation: fadeInDown 0.8s ease;
  }
  
  .hero-title {
    font-size: 4.5rem;
    font-weight: 800;
    margin: 0;
    letter-spacing: -2px;
    animation: fadeInUp 0.8s ease 0.2s backwards;
    text-shadow: 0 4px 30px rgba(0, 0, 0, 0.3);
  }
  
  .hero-subtitle {
    font-size: 1.4rem;
    margin-top: 16px;
    opacity: 0.9;
    letter-spacing: 8px;
    font-weight: 300;
    animation: fadeInUp 0.8s ease 0.4s backwards;
  }
  
  .hero-stats {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 40px;
    margin-top: 48px;
    animation: fadeInUp 0.8s ease 0.6s backwards;
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: 3rem;
        font-weight: 700;
        line-height: 1;
      }
      
      .stat-label {
        font-size: 0.85rem;
        opacity: 0.8;
        text-transform: uppercase;
        letter-spacing: 3px;
        margin-top: 8px;
        display: block;
      }
    }
    
    .stat-divider {
      width: 1px;
      height: 50px;
      background: rgba(255, 255, 255, 0.3);
    }
  }
  
  .scroll-indicator {
    position: absolute;
    bottom: 40px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 1;
    color: rgba(255, 255, 255, 0.8);
    cursor: pointer;
    text-align: center;
    transition: all 0.3s;
    
    span {
      display: block;
      font-size: 12px;
      letter-spacing: 2px;
      margin-bottom: 8px;
    }
    
    .arrow-icon {
      font-size: 24px;
      animation: bounceDown 2s infinite;
    }
    
    &:hover {
      color: #fff;
      transform: translateX(-50%) translateY(-5px);
    }
  }
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  50% { transform: translate(-30px, 30px) rotate(10deg); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(20px, -20px) scale(1.1); }
}

@keyframes float3 {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(-15px, 15px); }
}

@keyframes fadeInDown {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes bounceDown {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(10px); }
  60% { transform: translateY(5px); }
}

// Main Section
.main-section {
  padding: 80px 0;
  background: #f8fafc;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 40px;
}

// Posts Section
.posts-section {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 32px;
    
    h2 {
      font-size: 1.75rem;
      color: #1a1a2e;
      margin: 0;
      font-weight: 700;
    }
    
    .view-all {
      color: #667eea;
      text-decoration: none;
      display: flex;
      align-items: center;
      gap: 6px;
      font-weight: 500;
      padding: 8px 16px;
      border-radius: 8px;
      transition: all 0.3s;
      
      &:hover {
        background: rgba(102, 126, 234, 0.1);
      }
    }
  }
}

.post-card {
  display: flex;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  border: 1px solid #e5e7eb;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  
  &:hover {
    transform: translateY(-6px);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    border-color: transparent;
  }
  
  .post-cover {
    width: 220px;
    height: 165px;
    flex-shrink: 0;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.5s;
    }
    
    &.default-cover {
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      
      .el-icon {
        font-size: 52px;
        color: rgba(255, 255, 255, 0.9);
      }
    }
  }
  
  &:hover .post-cover img {
    transform: scale(1.08);
  }
  
  .post-info {
    flex: 1;
    padding: 24px;
    display: flex;
    flex-direction: column;
  }
  
  .post-category {
    margin-bottom: 12px;
    
    :deep(.el-tag) {
      border-radius: 6px;
      font-weight: 500;
    }
  }
  
  .post-title {
    font-size: 1.25rem;
    color: #1a1a2e;
    margin: 0 0 12px;
    line-height: 1.5;
    font-weight: 600;
    transition: color 0.3s;
    
    &:hover {
      color: #667eea;
    }
  }
  
  .post-summary {
    flex: 1;
    color: #6b7280;
    font-size: 0.95rem;
    line-height: 1.7;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  .post-meta {
    display: flex;
    gap: 24px;
    margin-top: 16px;
    color: #9ca3af;
    font-size: 0.85rem;
    
    span {
      display: flex;
      align-items: center;
      gap: 6px;
    }
  }
}

// Sidebar
.sidebar {
  .sidebar-card {
    background: #fff;
    border-radius: 16px;
    padding: 24px;
    margin-bottom: 24px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    border: 1px solid #e5e7eb;
    transition: all 0.3s;
    
    &:hover {
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
    }
  }
  
  .sidebar-title {
    font-size: 1rem;
    color: #1a1a2e;
    margin: 0 0 20px;
    display: flex;
    align-items: center;
    gap: 10px;
    font-weight: 600;
    
    .el-icon {
      color: #667eea;
      font-size: 18px;
    }
  }
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
  
  li {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 12px;
    border-radius: 10px;
    cursor: pointer;
    color: #4b5563;
    transition: all 0.3s;
    margin-bottom: 4px;
    
    &:hover {
      background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
      color: #667eea;
      padding-left: 16px;
    }
    
    .el-icon {
      font-size: 12px;
      opacity: 0;
      transition: all 0.3s;
    }
    
    &:hover .el-icon {
      opacity: 1;
    }
  }
}

.about-card {
  text-align: center;
  
  .about-avatar {
    margin-bottom: 16px;
    
    .avatar-img {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      object-fit: cover;
      box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
      border: 3px solid #fff;
    }
    
    :deep(.el-avatar) {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
    }
  }
  
  .about-name {
    font-size: 1.25rem;
    color: #1a1a2e;
    margin: 0 0 8px;
    font-weight: 700;
  }
  
  .about-bio {
    color: #6b7280;
    font-size: 0.9rem;
    margin: 0 0 20px;
    line-height: 1.6;
  }
  
  .about-links {
    display: flex;
    justify-content: center;
    gap: 12px;
    
    :deep(.el-button.is-circle) {
      border: 1px solid #e5e7eb;
      
      &:hover {
        background: #667eea;
        border-color: #667eea;
        color: #fff;
      }
    }
  }
}

// Responsive
@media (max-width: 992px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }
  
  .sidebar {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
    
    .sidebar-card {
      margin-bottom: 0;
    }
  }
  
  .hero-title {
    font-size: 3rem;
  }
  
  .hero-stats {
    gap: 24px;
    
    .stat-value {
      font-size: 2rem;
    }
  }
}

@media (max-width: 768px) {
  .sidebar {
    grid-template-columns: 1fr;
  }
  
  .post-card {
    flex-direction: column;
    
    .post-cover {
      width: 100%;
      height: 200px;
    }
  }
  
  .hero-title {
    font-size: 2.5rem;
    letter-spacing: -1px;
  }
  
  .hero-subtitle {
    font-size: 1rem;
    letter-spacing: 4px;
  }
  
  .hero-badge {
    font-size: 12px;
    padding: 6px 16px;
  }
}
</style>
