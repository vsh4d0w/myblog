<template>
  <div class="post-detail">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <el-skeleton-item variant="h1" style="width: 60%; height: 32px" />
        <div style="margin: 20px 0">
          <el-skeleton-item variant="text" style="width: 30%" />
        </div>
        <el-skeleton-item variant="rect" style="width: 100%; height: 400px" />
      </template>
      
      <template #default>
        <article v-if="post" class="post-article">
          <header class="post-header">
            <h1>{{ post.title }}</h1>
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
              <span class="views">
                <el-icon><View /></el-icon>
                {{ post.viewCount }} 阅读
              </span>
            </div>
          </header>
          
          <div class="post-content" v-html="post.contentHtml"></div>
          
          <!-- 评论区 -->
          <section class="comment-section">
            <h3>评论 ({{ comments.length }})</h3>
            
            <!-- 发表评论 -->
            <div v-if="userStore.isLoggedIn" class="comment-form">
              <el-input
                v-model="newComment"
                type="textarea"
                :rows="3"
                placeholder="写下你的评论..."
              />
              <el-button
                type="primary"
                :loading="submitting"
                @click="submitComment"
              >
                发表评论
              </el-button>
            </div>
            <div v-else class="login-tip">
              <router-link to="/login">登录</router-link> 后发表评论
            </div>
            
            <!-- 评论列表 -->
            <div class="comment-list">
              <div
                v-for="comment in comments"
                :key="comment.id"
                class="comment-item"
              >
                <div class="comment-header">
                  <el-avatar 
                    :size="40" 
                    :src="getImageUrl(comment.authorAvatar)" 
                    :icon="UserFilled" 
                  />
                  <div class="comment-info">
                    <span class="comment-author">{{ comment.authorNickname || comment.authorName }}</span>
                    <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                  </div>
                </div>
                <div class="comment-body">{{ comment.content }}</div>
              </div>
              
              <el-empty v-if="comments.length === 0" description="暂无评论" />
            </div>
          </section>
        </article>
        
        <el-empty v-else description="文章不存在" />
      </template>
    </el-skeleton>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getPostDetail } from '@/api/post'
import { getCommentsByPost, createComment } from '@/api/comment'
import { User, Calendar, Folder, View, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getImageUrl } from '@/utils/common'

const route = useRoute()
const userStore = useUserStore()

const loading = ref(true)
const post = ref(null)
const comments = ref([])
const newComment = ref('')
const submitting = ref(false)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const fetchPost = async () => {
  loading.value = true
  try {
    const res = await getPostDetail(route.params.id)
    if (res.code === 200) {
      post.value = res.data
    }
  } catch (error) {
    console.error('获取文章失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchComments = async () => {
  try {
    const res = await getCommentsByPost(route.params.id)
    if (res.code === 200) {
      comments.value = res.data || []
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  submitting.value = true
  try {
    const res = await createComment({
      postId: route.params.id,
      content: newComment.value
    })
    
    if (res.code === 200) {
      ElMessage.success('评论成功')
      newComment.value = ''
      fetchComments()
    } else {
      ElMessage.error(res.message || '评论失败')
    }
  } catch (error) {
    ElMessage.error('评论失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchPost()
  fetchComments()
})
</script>

<style scoped lang="scss">
.post-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.post-article {
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.post-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  
  h1 {
    font-size: 28px;
    color: #303133;
    margin: 0 0 15px;
  }
  
  .post-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    color: #909399;
    font-size: 14px;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }
}

.post-content {
  line-height: 1.8;
  color: #303133;
  
  :deep(h1),
  :deep(h2),
  :deep(h3),
  :deep(h4) {
    margin: 20px 0 10px;
    color: #303133;
  }
  
  :deep(p) {
    margin: 10px 0;
  }
  
  :deep(code) {
    background: #f5f7fa;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: monospace;
  }
  
  :deep(pre) {
    background: #2d2d2d;
    color: #ccc;
    padding: 15px;
    border-radius: 8px;
    overflow-x: auto;
    
    code {
      background: none;
      padding: 0;
    }
  }
  
  :deep(blockquote) {
    border-left: 4px solid #409eff;
    padding-left: 15px;
    margin: 15px 0;
    color: #606266;
  }
  
  :deep(img) {
    max-width: 100%;
    border-radius: 8px;
  }
}

.comment-section {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 1px solid #ebeef5;
  
  h3 {
    font-size: 20px;
    color: #303133;
    margin-bottom: 20px;
  }
}

.comment-form {
  margin-bottom: 30px;
  
  .el-button {
    margin-top: 10px;
  }
}

.login-tip {
  padding: 20px;
  text-align: center;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 30px;
  color: #909399;
  
  a {
    color: #409eff;
    text-decoration: none;
  }
}

.comment-list {
  .comment-item {
    padding: 15px 0;
    border-bottom: 1px solid #ebeef5;
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  .comment-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 10px;
  }
  
  .comment-info {
    display: flex;
    flex-direction: column;
    
    .comment-author {
      font-weight: 500;
      color: #303133;
    }
    
    .comment-time {
      font-size: 12px;
      color: #909399;
    }
  }
  
  .comment-body {
    margin-left: 48px;
    color: #606266;
    line-height: 1.6;
  }
}
</style>
