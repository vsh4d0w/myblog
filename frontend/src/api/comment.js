import request from '@/utils/request'

// 获取博文评论
export function getCommentsByPost(postId) {
  return request.get(`/comments/post/${postId}`)
}

// 获取评论回复
export function getCommentReplies(commentId) {
  return request.get(`/comments/${commentId}/replies`)
}

// 创建评论
export function createComment(data) {
  return request.post('/comments', data)
}

// 删除评论
export function deleteComment(id) {
  return request.delete(`/comments/${id}`)
}
