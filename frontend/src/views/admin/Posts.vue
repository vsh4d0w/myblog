<template>
  <div class="admin-posts">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" :icon="Plus" @click="openEditor()">
        新建文章
      </el-button>
    </div>
    
    <!-- 文章列表 -->
    <el-table :data="posts" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200">
        <template #default="{ row }">
          <el-link type="primary" @click="goToPost(row.id)">
            {{ row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="authorName" label="作者" width="120" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column prop="viewCount" label="浏览" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEditor(row)">编辑</el-button>
          <el-popconfirm title="确定删除这篇文章吗？" @confirm="handleDelete(row.id)">
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
        @current-change="fetchPosts"
        @size-change="fetchPosts"
      />
    </div>
    
    <!-- 编辑弹窗 -->
    <el-dialog
      v-model="editorVisible"
      :title="editingPost?.id ? '编辑文章' : '新建文章'"
      width="800px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="postForm" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入标题" />
        </el-form-item>
        
        <el-form-item label="分类" prop="category">
          <el-select v-model="postForm.category" placeholder="请选择分类">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.name"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="内容" prop="content">
          <div class="editor-toolbar">
            <span class="tip">支持 Markdown 格式，直接粘贴带有本地图片路径的内容，保存时会自动处理图片</span>
          </div>
          <el-input
            ref="contentRef"
            v-model="postForm.content"
            type="textarea"
            :rows="15"
            placeholder="支持 Markdown 格式，例如: ![image](/Users/.../image.png)"
          />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-radio-group v-model="postForm.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="editorVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="savePost">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPosts, createPost, updatePost, deletePost } from '@/api/post'
import { getCategories } from '@/api/category'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const posts = ref([])
const categories = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const editorVisible = ref(false)
const editingPost = ref(null)
const saving = ref(false)
const formRef = ref(null)
const contentRef = ref(null)

const postForm = reactive({
  title: '',
  content: '',
  category: '',
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
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
    const res = await getPosts({ page: page.value, size: size.value })
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

const openEditor = (post = null) => {
  editingPost.value = post
  if (post) {
    postForm.title = post.title
    postForm.content = post.content
    postForm.category = post.categoryName || post.category || ''
    postForm.status = post.status
  } else {
    postForm.title = ''
    postForm.content = ''
    postForm.category = ''
    postForm.status = 1
  }
  editorVisible.value = true
}

const savePost = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  saving.value = true
  try {
    let res
    if (editingPost.value?.id) {
      // 更新时需要将 id 包含在请求体中
      res = await updatePost({ ...postForm, id: editingPost.value.id })
    } else {
      res = await createPost(postForm)
    }
    
    if (res.code === 200) {
      ElMessage.success('保存成功')
      editorVisible.value = false
      fetchPosts()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const handleDelete = async (id) => {
  try {
    const res = await deletePost(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchPosts()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const goToPost = (id) => {
  window.open(`/post/${id}`, '_blank')
}

onMounted(() => {
  fetchCategories()
  fetchPosts()
})
</script>

<style scoped lang="scss">
.admin-posts {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h2 {
      font-size: 24px;
      color: #303133;
      margin: 0;
    }
  }
  
  .el-table {
    background: #fff;
    border-radius: 8px;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.editor-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
  
  .tip {
    font-size: 12px;
    color: #909399;
  }
}
</style>
