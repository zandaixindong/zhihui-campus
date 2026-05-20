import request from '@/utils/request'

// ==================== 公告管理 ====================

/**
 * 分页查询公告列表
 */
export function getAnnouncementList(params?: { noticeType?: number; collegeId?: number; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/notice/announcement/list',
    method: 'get',
    params
  })
}

/**
 * 查询公告详情
 */
export function getAnnouncementDetail(id: number) {
  return request({
    url: `/notice/announcement/${id}`,
    method: 'get'
  })
}

/**
 * 发布公告
 */
export function publishAnnouncement(data: any) {
  return request({
    url: '/notice/announcement',
    method: 'post',
    data
  })
}

/**
 * 修改公告
 */
export function updateAnnouncement(data: any) {
  return request({
    url: '/notice/announcement',
    method: 'put',
    data
  })
}

/**
 * 删除公告
 */
export function deleteAnnouncement(id: number) {
  return request({
    url: `/notice/announcement/${id}`,
    method: 'delete'
  })
}

// ==================== 消息管理 ====================

/**
 * 分页查询消息列表
 */
export function getMessageList(params: { userId: number; msgType?: number; isRead?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/notice/message/list',
    method: 'get',
    params
  })
}

/**
 * 统计未读消息数量
 */
export function getUnreadCount(userId: number) {
  return request({
    url: '/notice/message/unread/count',
    method: 'get',
    params: { userId }
  })
}

/**
 * 发送消息
 */
export function sendMessage(data: any) {
  return request({
    url: '/notice/message',
    method: 'post',
    data
  })
}

/**
 * 标记已读
 */
export function markAsRead(messageId: number) {
  return request({
    url: `/notice/message/read/${messageId}`,
    method: 'put'
  })
}

/**
 * 标记全部已读
 */
export function markAllAsRead(userId: number) {
  return request({
    url: '/notice/message/read/all',
    method: 'put',
    params: { userId }
  })
}
