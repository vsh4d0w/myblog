import request from '@/utils/request'

// 渲染 Markdown 为 HTML
export function renderMarkdown(markdown) {
  return request.post('/markdown/render', { markdown })
}

// 提取 Markdown 摘要
export function extractSummary(markdown, maxLength = 200) {
  return request.post('/markdown/summary', { markdown, maxLength })
}
