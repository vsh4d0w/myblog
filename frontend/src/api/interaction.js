import request from '@/utils/request'

/**
 * 点赞博文
 */
export function likePost(postId) {
  return request({
    url: `/interaction/like/${postId}`,
    method: 'post'
  })
}

/**
 * 取消点赞
 */
export function unlikePost(postId) {
  return request({
    url: `/interaction/like/${postId}`,
    method: 'delete'
  })
}

/**
 * 收藏博文
 */
export function favoritePost(postId) {
  return request({
    url: `/interaction/favorite/${postId}`,
    method: 'post'
  })
}

/**
 * 取消收藏
 */
export function unfavoritePost(postId) {
  return request({
    url: `/interaction/favorite/${postId}`,
    method: 'delete'
  })
}

/**
 * 获取博文互动状态
 */
export function getInteractionStatus(postId) {
  return request({
    url: `/interaction/status/${postId}`,
    method: 'get'
  })
}
