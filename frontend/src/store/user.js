import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, getCurrentUser } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')
  const username = computed(() => userInfo.value?.username || '')
  const nickname = computed(() => userInfo.value?.nickname || userInfo.value?.username || '游客')
  
  // 设置 Token
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  
  // 设置用户信息
  function setUserInfo(info) {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }
  
  // 登录
  async function loginAction(loginData) {
    const res = await login(loginData)
    if (res.code === 200) {
      setToken(res.data.token)
      setUserInfo(res.data.user)
      return res
    }
    throw new Error(res.message || '登录失败')
  }
  
  // 注册
  async function registerAction(registerData) {
    const res = await register(registerData)
    if (res.code === 200) {
      return res
    }
    throw new Error(res.message || '注册失败')
  }
  
  // 获取当前用户信息
  async function fetchUserInfo() {
    if (!token.value) return null
    try {
      const res = await getCurrentUser()
      if (res.code === 200) {
        setUserInfo(res.data)
        return res.data
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
      logout()
    }
    return null
  }
  
  // 登出
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }
  
  return {
    // 状态
    token,
    userInfo,
    // 计算属性
    isLoggedIn,
    isAdmin,
    username,
    nickname,
    // 方法
    setToken,
    setUserInfo,
    loginAction,
    registerAction,
    fetchUserInfo,
    logout
  }
})
