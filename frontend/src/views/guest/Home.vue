<template>
  <div class="home">
    <!-- Hero Section with Background -->
    <section class="hero">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <h1 class="hero-title">sh4d0w's Blog</h1>
        <p class="hero-subtitle">探索 · 学习 · 分享</p>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-value">{{ stats.postCount }}</span>
            <span class="stat-label">文章</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.tagCount }}</span>
            <span class="stat-label">标签</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.viewCount }}</span>
            <span class="stat-label">浏览</span>
          </div>
        </div>
      </div>
      <div class="scroll-down" @click="scrollToContent">
        <el-icon><ArrowDown /></el-icon>
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
          <!-- 热门标签 -->
          <div class="sidebar-card">
            <h3 class="sidebar-title">
              <el-icon><PriceTag /></el-icon> 热门标签
            </h3>
            <div class="tag-cloud">
              <el-tag
                v-for="tag in tags"
                :key="tag.id"
                :style="{ backgroundColor: tag.color, borderColor: tag.color }"
                class="tag-item"
                @click="searchByTag(tag.name)"
              >
                {{ tag.name }}
                <span class="tag-count">{{ tag.useCount }}</span>
              </el-tag>
              <el-empty v-if="tags.length === 0" description="暂无标签" :image-size="60" />
            </div>
          </div>
          
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
              <el-avatar :size="80" :icon="UserFilled" />
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
import { getHotTags } from '@/api/tag'
import { 
  ArrowDown, ArrowRight, Document, Calendar, View, 
  PriceTag, Folder, UserFilled, Link 
} from '@element-plus/icons-vue'

const router = useRouter()

const loading = ref(true)
const posts = ref([])
const tags = ref([])
const contentRef = ref(null)

const stats = ref({
  postCount: 0,
  tagCount: 0,
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

const searchByTag = (tagName) => {
  router.push({ path: '/search', query: { keyword: tagName } })
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
    
    // 获取热门标签
    try {
      const tagRes = await getHotTags(10)
      if (tagRes.code === 200) {
        tags.value = tagRes.data || []
        stats.value.tagCount = tags.value.length
      }
    } catch (e) {
      // 标签API可能未实现，忽略错误
      console.log('标签API暂不可用')
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
}

// Hero Section
.hero {
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background: url('@/assets/images/bg.jpg') center/cover no-repeat fixed;
  
  .hero-overlay {
    position: absolute;
    inset: 0;
    background: linear-gradient(
      135deg,
      rgba(0, 0, 0, 0.6) 0%,
      rgba(0, 0, 0, 0.3) 100%
    );
  }
  
  .hero-content {
    position: relative;
    z-index: 1;
    text-align: center;
    color: #fff;
  }
  
  .hero-title {
    font-size: 4rem;
    font-weight: 700;
    margin: 0;
    text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.5);
    animation: fadeInUp 1s ease;
  }
  
  .hero-subtitle {
    font-size: 1.5rem;
    margin-top: 15px;
    opacity: 0.9;
    letter-spacing: 5px;
    animation: fadeInUp 1s ease 0.2s backwards;
  }
  
  .hero-stats {
    display: flex;
    justify-content: center;
    gap: 60px;
    margin-top: 40px;
    animation: fadeInUp 1s ease 0.4s backwards;
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: 2.5rem;
        font-weight: 700;
      }
      
      .stat-label {
        font-size: 0.9rem;
        opacity: 0.8;
        text-transform: uppercase;
        letter-spacing: 2px;
      }
    }
  }
  
  .scroll-down {
    position: absolute;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 1;
    color: #fff;
    font-size: 2rem;
    cursor: pointer;
    animation: bounce 2s infinite;
    
    &:hover {
      color: #409eff;
    }
  }
}

// Main Section
.main-section {
  padding: 60px 0;
  background: #f5f7fa;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 30px;
}

// Posts Section
.posts-section {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    
    h2 {
      font-size: 1.5rem;
      color: #303133;
      margin: 0;
    }
    
    .view-all {
      color: #409eff;
      text-decoration: none;
      display: flex;
      align-items: center;
      gap: 5px;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.post-card {
  display: flex;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }
  
  .post-cover {
    width: 200px;
    height: 150px;
    flex-shrink: 0;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s;
    }
    
    &.default-cover {
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      
      .el-icon {
        font-size: 48px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
  
  &:hover .post-cover img {
    transform: scale(1.1);
  }
  
  .post-info {
    flex: 1;
    padding: 20px;
    display: flex;
    flex-direction: column;
  }
  
  .post-category {
    margin-bottom: 10px;
  }
  
  .post-title {
    font-size: 1.2rem;
    color: #303133;
    margin: 0 0 10px;
    line-height: 1.4;
    
    &:hover {
      color: #409eff;
    }
  }
  
  .post-summary {
    flex: 1;
    color: #606266;
    font-size: 0.9rem;
    line-height: 1.6;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  .post-meta {
    display: flex;
    gap: 20px;
    margin-top: 15px;
    color: #909399;
    font-size: 0.85rem;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }
}

// Sidebar
.sidebar {
  .sidebar-card {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }
  
  .sidebar-title {
    font-size: 1rem;
    color: #303133;
    margin: 0 0 15px;
    display: flex;
    align-items: center;
    gap: 8px;
    
    .el-icon {
      color: #409eff;
    }
  }
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  
  .tag-item {
    cursor: pointer;
    color: #fff;
    transition: all 0.3s;
    
    &:hover {
      transform: scale(1.1);
    }
    
    .tag-count {
      margin-left: 5px;
      opacity: 0.8;
      font-size: 0.8em;
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
    padding: 12px 0;
    border-bottom: 1px solid #ebeef5;
    cursor: pointer;
    color: #606266;
    transition: all 0.3s;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:hover {
      color: #409eff;
      padding-left: 10px;
    }
    
    .el-icon {
      font-size: 0.8rem;
    }
  }
}

.about-card {
  text-align: center;
  
  .about-avatar {
    margin-bottom: 15px;
  }
  
  .about-name {
    font-size: 1.2rem;
    color: #303133;
    margin: 0 0 8px;
  }
  
  .about-bio {
    color: #909399;
    font-size: 0.9rem;
    margin: 0 0 15px;
  }
  
  .about-links {
    display: flex;
    justify-content: center;
    gap: 10px;
  }
}

// Animations
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateX(-50%) translateY(0);
  }
  40% {
    transform: translateX(-50%) translateY(-15px);
  }
  60% {
    transform: translateX(-50%) translateY(-8px);
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
    gap: 20px;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-stats {
    gap: 30px;
    
    .stat-value {
      font-size: 1.8rem;
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
      height: 180px;
    }
  }
  
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
    letter-spacing: 2px;
  }
}
</style>
