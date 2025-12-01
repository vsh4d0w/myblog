<template>
  <div class="admin-users">
    <h2>用户管理</h2>
    
    <!-- 用户列表 -->
    <el-table :data="users" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="email" label="邮箱" min-width="200" />
      <el-table-column prop="roles" label="角色" width="150">
        <template #default="{ row }">
          <el-tag
            v-for="role in row.roles"
            :key="role.id || role.name"
            :type="role.name === 'ADMIN' ? 'danger' : 'info'"
            style="margin-right: 5px"
          >
            {{ role.name === 'ADMIN' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 1"
            type="warning"
            link
            @click="handleUpdateStatus(row.id, 0)"
          >
            禁用
          </el-button>
          <el-button
            v-else
            type="success"
            link
            @click="handleUpdateStatus(row.id, 1)"
          >
            启用
          </el-button>
          <el-popconfirm
            title="确定删除这个用户吗？"
            @confirm="handleDelete(row.id)"
          >
            <template #reference>
              <el-button type="danger" link :disabled="isAdmin(row)">
                删除
              </el-button>
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
        @current-change="fetchUsers"
        @size-change="fetchUsers"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllUsers, updateUserStatus, deleteUser } from '@/api/admin'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const users = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const isAdmin = (user) => {
  return user.roles?.some(role => role.name === 'ADMIN')
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getAllUsers({ page: page.value, size: size.value })
    if (res.code === 200) {
      users.value = res.data?.records || res.data || []
      total.value = res.data?.total || users.value.length
    }
  } catch (error) {
    console.error('获取用户失败:', error)
  } finally {
    loading.value = false
  }
}

const handleUpdateStatus = async (id, status) => {
  try {
    const res = await updateUserStatus(id, status)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
      fetchUsers()
    } else {
      ElMessage.error(res.message || '更新失败')
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const handleDelete = async (id) => {
  try {
    const res = await deleteUser(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchUsers()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped lang="scss">
.admin-users {
  h2 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 20px;
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
</style>
