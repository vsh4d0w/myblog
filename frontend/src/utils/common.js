/**
 * 通用工具函数
 */

// 后端API地址
export const API_BASE_URL = 'http://localhost:8080'

/**
 * 获取完整的图片URL
 * @param {string} url 图片路径
 * @returns {string} 完整URL
 */
export function getImageUrl(url) {
  if (!url) return ''
  // 如果已经是完整URL，直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  // 如果是相对路径，添加后端地址前缀
  if (url.startsWith('/')) {
    return API_BASE_URL + url
  }
  return url
}

/**
 * 获取用户头像URL，如果没有则返回默认头像
 * @param {string} avatar 头像路径
 * @returns {string} 头像URL
 */
export function getAvatarUrl(avatar) {
  if (!avatar) return ''
  return getImageUrl(avatar)
}
