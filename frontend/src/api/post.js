import request from '@/utils/request'

// 获取博文列表
export function getPosts(params) {
  return request.get('/posts', { params })
}

// 获取博文详情
export function getPostDetail(id) {
  return request.get(`/posts/${id}`)
}

// 按分类获取博文
export function getPostsByCategory(category, params) {
  return request.get(`/posts/category/${category}`, { params })
}

// 搜索博文
export function searchPosts(keyword, params) {
  return request.get('/posts/search', { params: { keyword, ...params } })
}

// 创建博文 (管理员)
export function createPost(data) {
  return request.post('/posts', data)
}

// 更新博文 (管理员)
export function updatePost(data) {
  return request.put('/posts', data)
}

// 删除博文 (管理员)
export function deletePost(id) {
  return request.delete(`/posts/${id}`)
}
