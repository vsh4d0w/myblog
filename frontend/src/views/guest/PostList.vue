<template>
  <div class="post-list">
    <h2>全部文章</h2>
    
    <!-- 分类筛选 -->
    <div class="filter-bar">
      <el-radio-group v-model="currentCategory" @change="handleCategoryChange">
        <el-radio-button :label="null">全部</el-radio-button>
        <el-radio-button
          v-for="cat in categories"
          :key="cat.id"
          :label="cat.id"
        >
          {{ cat.name }}
        </el-radio-button>
      </el-radio-group>
    </div>
    
    <!-- 文章列表 -->
    <el-skeleton :loading="loading" animated :count="5">
      <template #template>
        <div style="padding: 20px; background: #fff; margin-bottom: 15px; border-radius: 8px;">
          <el-skeleton-item variant="h3" style="width: 50%" />
          <el-skeleton-item variant="text" style="margin-top: 15px" />
          <el-skeleton-item variant="text" style="width: 60%" />
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
              <span v-if="post.categoryName" class="category">
                <el-icon><Folder /></el-icon>
                {{ post.categoryName }}
              </span>
            </div>
          </div>
          
          <el-empty v-if="posts.length === 0" description="暂无文章" />
        </div>
        
        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="page"
            v-model:page-size="size"
            :total="total"
            :page-sizes="[10, 20, 30]"
            layout="total, sizes, prev, pager, next"
            @current-change="fetchPosts"
            @size-change="fetchPosts"
          />
        </div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPosts, getPostsByCategory } from '@/api/post'
import { getCategories } from '@/api/category'
import { User, Calendar, Folder } from '@element-plus/icons-vue'

const router = useRouter()

const loading = ref(true)
const posts = ref([])
const categories = ref([])
const currentCategory = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const fetchCategories = async () => {
  try {
    const res = await getCategories()
    if (res.code === 200) {
      categories.value = res.data || []
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

const fetchPosts = async () => {
  loading.value = true
  try {
    let res
    if (currentCategory.value) {
      res = await getPostsByCategory(currentCategory.value, {
        page: page.value,
        size: size.value
      })
    } else {
      res = await getPosts({
        page: page.value,
        size: size.value
      })
    }
    
    if (res.code === 200) {
      posts.value = res.data?.records || res.data || []
      total.value = res.data?.total || posts.value.length
    }
  } catch (error) {
    console.error('获取文章失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCategoryChange = () => {
  page.value = 1
  fetchPosts()
}

const goToPost = (id) => {
  router.push(`/post/${id}`)
}

onMounted(() => {
  fetchCategories()
  fetchPosts()
})
</script>

<style scoped lang="scss">
.post-list {
  max-width: 900px;
  margin: 0 auto;
  
  h2 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 20px;
  }
}

.filter-bar {
  margin-bottom: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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
      
      &:hover {
        color: #409eff;
      }
    }
    
    .post-summary {
      color: #606266;
      font-size: 14px;
      line-height: 1.6;
      margin: 0 0 15px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
