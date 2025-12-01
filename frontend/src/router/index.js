import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

// 路由配置
const routes = [
  // ========== 公共页面 ==========
  {
    path: '/',
    component: () => import('@/layouts/GuestLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/guest/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'post/:id',
        name: 'PostDetail',
        component: () => import('@/views/guest/PostDetail.vue'),
        meta: { title: '博文详情' }
      },
      {
        path: 'posts',
        name: 'PostList',
        component: () => import('@/views/guest/PostList.vue'),
        meta: { title: '文章列表' }
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import('@/views/guest/Search.vue'),
        meta: { title: '搜索' }
      }
    ]
  },
  
  // ========== 认证页面 ==========
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { title: '登录', guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { title: '注册', guest: true }
  },
  
  // ========== 管理员页面 (需要 ADMIN 角色) ==========
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '管理后台' }
      },
      {
        path: 'posts',
        name: 'AdminPosts',
        component: () => import('@/views/admin/Posts.vue'),
        meta: { title: '博文管理' }
      },
      {
        path: 'comments',
        name: 'AdminComments',
        component: () => import('@/views/admin/Comments.vue'),
        meta: { title: '评论管理' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
        meta: { title: '用户管理' }
      }
    ]
  },
  
  // ========== 404 页面 ==========
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - myBlog` : 'myBlog'
  
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn
  const isAdmin = userStore.isAdmin
  
  // 需要登录的页面
  if (to.meta.requiresAuth && !isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }
  
  // 需要管理员权限的页面
  if (to.meta.requiresAdmin && !isAdmin) {
    next({ name: 'Home' })
    return
  }
  
  // 已登录用户访问登录/注册页面，重定向
  if (to.meta.guest && isLoggedIn) {
    if (isAdmin) {
      next({ name: 'AdminDashboard' })
    } else {
      next({ name: 'Home' })
    }
    return
  }
  
  next()
})

export default router
