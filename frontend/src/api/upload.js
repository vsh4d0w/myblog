import request from '@/utils/request'

/**
 * 上传图片
 */
export function uploadImage(file, type = 'images') {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', type)
  
  return request({
    url: '/upload/image',
    method: 'post',
    data: formData
  })
}

/**
 * 上传头像
 */
export function uploadAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/upload/avatar',
    method: 'post',
    data: formData
  })
}

/**
 * 上传封面图
 */
export function uploadCover(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/upload/cover',
    method: 'post',
    data: formData
  })
}
