<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="card-header">
        <h2>个人中心</h2>
        <p class="subtitle">管理您的个人信息</p>
      </div>
      
      <!-- 头像区域 -->
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <el-avatar :size="100" :src="avatarUrl" :icon="UserFilled" />
          <el-upload
            class="avatar-upload"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadAvatarHandler"
          >
            <div class="avatar-overlay">
              <el-icon><Camera /></el-icon>
              <span>更换头像</span>
            </div>
          </el-upload>
        </div>
        <div class="user-info">
          <h3>{{ userStore.nickname }}</h3>
          <el-tag :type="userStore.isAdmin ? 'danger' : 'info'" size="small">
            {{ userStore.isAdmin ? '管理员' : '游客' }}
          </el-tag>
        </div>
      </div>
      
      <!-- 信息修改表单 -->
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="基本信息" name="info">
          <el-form
            ref="infoFormRef"
            :model="infoForm"
            :rules="infoRules"
            label-width="80px"
            class="profile-form"
          >
            <el-form-item label="用户名">
              <el-input v-model="userStore.username" disabled />
            </el-form-item>
            
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="infoForm.nickname" placeholder="请输入昵称" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" :loading="infoLoading" @click="handleUpdateInfo">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="修改密码" name="password">
          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="80px"
            class="profile-form"
          >
            <el-form-item label="原密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入原密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请确认新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" :loading="passwordLoading" @click="handleUpdatePassword">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      
      <div class="back-link">
        <router-link to="/">← 返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useUserStore } from '@/store/user'
import { updateProfile, updatePassword } from '@/api/auth'
import { uploadAvatar } from '@/api/upload'
import { UserFilled, Camera } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

const activeTab = ref('info')
const infoFormRef = ref(null)
const passwordFormRef = ref(null)
const infoLoading = ref(false)
const passwordLoading = ref(false)

// 后端地址
const API_BASE = 'http://localhost:8080'

// 头像URL计算 - 处理相对路径
const avatarUrl = computed(() => {
  const avatar = userStore.userInfo?.avatar
  if (!avatar) return ''
  // 如果是相对路径，添加后端地址前缀
  if (avatar.startsWith('/')) {
    return API_BASE + avatar
  }
  return avatar
})

// 基本信息表单
const infoForm = reactive({
  nickname: userStore.userInfo?.nickname || ''
})

const infoRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2-20 个字符', trigger: 'blur' }
  ]
}

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 上传头像前的验证
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }
  return true
}

// 上传头像
const uploadAvatarHandler = async ({ file }) => {
  try {
    const res = await uploadAvatar(file)
    if (res.code === 200) {
      // 更新头像到用户信息
      const updateRes = await updateProfile({ avatar: res.data.url })
      if (updateRes.code === 200) {
        userStore.setUserInfo(updateRes.data)
        ElMessage.success('头像更新成功')
      }
    } else {
      ElMessage.error(res.message || '上传失败')
    }
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

// 更新基本信息
const handleUpdateInfo = async () => {
  const valid = await infoFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  infoLoading.value = true
  try {
    const res = await updateProfile({ nickname: infoForm.nickname })
    if (res.code === 200) {
      userStore.setUserInfo(res.data)
      ElMessage.success('信息更新成功')
    } else {
      ElMessage.error(res.message || '更新失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '更新失败')
  } finally {
    infoLoading.value = false
  }
}

// 修改密码
const handleUpdatePassword = async () => {
  const valid = await passwordFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  passwordLoading.value = true
  try {
    const res = await updatePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    if (res.code === 200) {
      ElMessage.success('密码修改成功，请重新登录')
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
      // 退出登录
      setTimeout(() => {
        userStore.logout()
      }, 1500)
    } else {
      ElMessage.error(res.message || '修改失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '修改失败')
  } finally {
    passwordLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.profile-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.profile-card {
  width: 100%;
  max-width: 500px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 40px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.card-header {
  text-align: center;
  margin-bottom: 30px;
  
  h2 {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    color: #1a1a2e;
  }
  
  .subtitle {
    margin: 8px 0 0;
    color: #6b7280;
    font-size: 14px;
  }
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
  
  .avatar-wrapper {
    position: relative;
    cursor: pointer;
    
    .avatar-overlay {
      position: absolute;
      inset: 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background: rgba(0, 0, 0, 0.5);
      border-radius: 50%;
      opacity: 0;
      transition: opacity 0.3s;
      color: #fff;
      font-size: 12px;
      
      .el-icon {
        font-size: 24px;
        margin-bottom: 4px;
      }
    }
    
    &:hover .avatar-overlay {
      opacity: 1;
    }
  }
  
  .avatar-upload {
    position: absolute;
    inset: 0;
  }
  
  .user-info {
    text-align: center;
    margin-top: 16px;
    
    h3 {
      margin: 0 0 8px;
      font-size: 20px;
      color: #1a1a2e;
    }
  }
}

.profile-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 24px;
  }
  
  :deep(.el-tabs__nav-wrap::after) {
    display: none;
  }
  
  :deep(.el-tabs__item) {
    font-size: 15px;
    
    &.is-active {
      color: #667eea;
    }
  }
  
  :deep(.el-tabs__active-bar) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
}

.profile-form {
  :deep(.el-input__wrapper) {
    border-radius: 10px;
  }
  
  :deep(.el-button) {
    border-radius: 10px;
    height: 44px;
    font-size: 15px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    
    &:hover {
      opacity: 0.9;
    }
  }
}

.back-link {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
  
  a {
    color: #667eea;
    text-decoration: none;
    font-size: 14px;
    
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
