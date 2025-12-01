import request from '@/utils/request'

/**
 * 获取所有标签
 */
export function getAllTags() {
  return request({
    url: '/tags',
    method: 'get'
  })
}

/**
 * 获取热门标签
 */
export function getHotTags(limit = 10) {
  return request({
    url: '/tags/hot',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取博文的标签
 */
export function getPostTags(postId) {
  return request({
    url: `/tags/post/${postId}`,
    method: 'get'
  })
}

/**
 * 创建标签（管理员）
 */
export function createTag(data) {
  return request({
    url: '/tags',
    method: 'post',
    data
  })
}

/**
 * 删除标签（管理员）
 */
export function deleteTag(id) {
  return request({
    url: `/tags/${id}`,
    method: 'delete'
  })
}
