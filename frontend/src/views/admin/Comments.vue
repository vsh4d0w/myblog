<template>
  <div class="admin-comments">
    <h2>评论管理</h2>
    
    <!-- 评论列表 -->
    <el-table :data="comments" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="content" label="评论内容" min-width="250">
        <template #default="{ row }">
          <div class="comment-content">{{ row.content }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="authorName" label="评论者" width="120" />
      <el-table-column prop="postTitle" label="所属文章" width="200">
        <template #default="{ row }">
          <el-link type="primary" @click="goToPost(row.postId)">
            {{ row.postTitle || `文章 #${row.postId}` }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="评论时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status !== 1"
            type="success"
            link
            @click="handleUpdateStatus(row.id, 1)"
          >
            通过
          </el-button>
          <el-button
            v-if="row.status !== 2"
            type="warning"
            link
            @click="handleUpdateStatus(row.id, 2)"
          >
            隐藏
          </el-button>
          <el-popconfirm title="确定删除这条评论吗？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button type="danger" link>删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @current-change="fetchComments"
        @size-change="fetchComments"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllComments, updateCommentStatus } from '@/api/admin'
import { deleteComment } from '@/api/comment'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const comments = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待审核', 1: '已通过', 2: '已隐藏' }
  return texts[status] || '未知'
}

const fetchComments = async () => {
  loading.value = true
  try {
    const res = await getAllComments({ page: page.value, size: size.value })
    if (res.code === 200) {
      comments.value = res.data?.records || res.data || []
      total.value = res.data?.total || comments.value.length
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  } finally {
    loading.value = false
  }
}

const handleUpdateStatus = async (id, status) => {
  try {
    const res = await updateCommentStatus(id, status)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
      fetchComments()
    } else {
      ElMessage.error(res.message || '更新失败')
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const handleDelete = async (id) => {
  try {
    const res = await deleteComment(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchComments()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const goToPost = (postId) => {
  window.open(`/post/${postId}`, '_blank')
}

onMounted(() => {
  fetchComments()
})
</script>

<style scoped lang="scss">
.admin-comments {
  h2 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 20px;
  }
  
  .el-table {
    background: #fff;
    border-radius: 8px;
  }
  
  .comment-content {
    max-width: 300px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
