import request from '@/utils/request'

// 获取所有用户
export function getAllUsers(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

// 更新用户状态
export function updateUserStatus(id, status) {
  return request({
    url: `/admin/users/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/admin/users/${id}`,
    method: 'delete'
  })
}

// 获取所有评论
export function getAllComments(params) {
  return request({
    url: '/admin/comments',
    method: 'get',
    params
  })
}

// 更新评论状态
export function updateCommentStatus(id, status) {
  return request({
    url: `/admin/comments/${id}/status`,
    method: 'put',
    params: { status }
  })
}
