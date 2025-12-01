<template>
  <div class="login-container">
    <!-- 装饰背景 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="login-card">
      <div class="card-header">
        <div class="logo">
          <span class="logo-icon">📝</span>
        </div>
        <h2>欢迎回来</h2>
        <p class="subtitle">登录到 sh4d0w's Blog</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="0"
        size="large"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名"
            :prefix-icon="User"
            class="custom-input"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
            class="custom-input"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item class="btn-item">
          <el-button
            type="primary"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    await userStore.loginAction(form)
    ElMessage.success('登录成功')
    
    // 如果是管理员，跳转到后台；否则跳转到之前的页面或首页
    const redirect = route.query.redirect || '/'
    if (userStore.isAdmin) {
      router.push('/admin')
    } else {
      router.push(redirect)
    }
  } catch (error) {
    ElMessage.error(error.message || '登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

// 装饰性背景圆圈
.bg-decoration {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
  
  .circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.08);
  }
  
  .circle-1 {
    width: 400px;
    height: 400px;
    top: -100px;
    right: -100px;
    animation: float 8s ease-in-out infinite;
  }
  
  .circle-2 {
    width: 300px;
    height: 300px;
    bottom: -50px;
    left: -80px;
    animation: float 10s ease-in-out infinite reverse;
  }
  
  .circle-3 {
    width: 200px;
    height: 200px;
    top: 50%;
    left: 10%;
    animation: float 6s ease-in-out infinite;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-20px) scale(1.05); }
}

.login-card {
  width: 420px;
  padding: 48px 40px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 
    0 25px 50px -12px rgba(0, 0, 0, 0.25),
    0 0 0 1px rgba(255, 255, 255, 0.1);
  position: relative;
  z-index: 1;
  animation: cardAppear 0.6s ease;
}

@keyframes cardAppear {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.card-header {
  text-align: center;
  margin-bottom: 36px;
  
  .logo {
    margin-bottom: 16px;
    
    .logo-icon {
      font-size: 48px;
      display: inline-block;
      animation: bounce 2s ease infinite;
    }
  }
  
  h2 {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    color: #1a1a2e;
    letter-spacing: -0.5px;
  }
  
  .subtitle {
    margin: 8px 0 0;
    color: #6b7280;
    font-size: 14px;
  }
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
}

.login-form {
  :deep(.el-form-item) {
    margin-bottom: 24px;
  }
  
  :deep(.el-input__wrapper) {
    border-radius: 12px;
    padding: 4px 16px;
    box-shadow: 0 0 0 1px #e5e7eb;
    transition: all 0.3s ease;
    
    &:hover {
      box-shadow: 0 0 0 1px #667eea;
    }
    
    &.is-focus {
      box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.3);
    }
  }
  
  :deep(.el-input__inner) {
    height: 48px;
    font-size: 15px;
  }
  
  :deep(.el-input__prefix) {
    font-size: 18px;
    color: #9ca3af;
  }
}

.btn-item {
  margin-top: 8px;
}

.login-btn {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);
  }
  
  &:active {
    transform: translateY(0);
  }
}

.login-footer {
  text-align: center;
  margin-top: 28px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
  color: #6b7280;
  font-size: 14px;
  
  a {
    color: #667eea;
    text-decoration: none;
    font-weight: 600;
    margin-left: 4px;
    transition: all 0.3s;
    
    &:hover {
      color: #764ba2;
    }
  }
}

// 响应式
@media (max-width: 480px) {
  .login-card {
    width: 90%;
    padding: 32px 24px;
    border-radius: 20px;
  }
  
  .card-header h2 {
    font-size: 24px;
  }
}
</style>
