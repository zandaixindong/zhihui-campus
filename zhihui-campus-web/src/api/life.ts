import request from '@/utils/request'

// ==================== 一卡通 ====================

/**
 * 查询余额
 */
export function getCardBalance(userId: number) {
  return request({
    url: '/life/card/balance',
    method: 'get',
    params: { userId }
  })
}

/**
 * 查询消费记录
 */
export function getCardTransactions(params: { userId: number; transactionType?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/life/card/transactions',
    method: 'get',
    params
  })
}

/**
 * 充值
 */
export function recharge(userId: number, amount: number) {
  return request({
    url: '/life/card/recharge',
    method: 'post',
    params: { userId, amount }
  })
}

// ==================== 报修服务 ====================

/**
 * 分页查询报修工单
 */
export function getRepairList(params?: { userId?: number; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/life/repair/list',
    method: 'get',
    params
  })
}

/**
 * 查询工单详情
 */
export function getRepairDetail(orderId: number) {
  return request({
    url: `/life/repair/${orderId}`,
    method: 'get'
  })
}

/**
 * 提交报修工单
 */
export function submitRepair(data: any) {
  return request({
    url: '/life/repair',
    method: 'post',
    data
  })
}

/**
 * 取消报修工单
 */
export function cancelRepair(orderId: number) {
  return request({
    url: `/life/repair/cancel/${orderId}`,
    method: 'put'
  })
}

// ==================== 场地预约 ====================

/**
 * 查询场地列表
 */
export function getVenueList(venueType?: string) {
  return request({
    url: '/life/venue/list',
    method: 'get',
    params: { venueType }
  })
}

/**
 * 查询场地详情
 */
export function getVenueDetail(venueId: number) {
  return request({
    url: `/life/venue/${venueId}`,
    method: 'get'
  })
}

/**
 * 查询场地可用时间段
 */
export function getVenueAvailableSlots(venueId: number, date: string) {
  return request({
    url: `/life/venue/${venueId}/available`,
    method: 'get',
    params: { date }
  })
}

/**
 * 分页查询预约记录
 */
export function getVenueBookings(params?: { userId?: number; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/life/venue/booking/list',
    method: 'get',
    params
  })
}

/**
 * 提交预约申请
 */
export function submitVenueBooking(data: any) {
  return request({
    url: '/life/venue/booking',
    method: 'post',
    data
  })
}

/**
 * 取消预约
 */
export function cancelVenueBooking(bookingId: number) {
  return request({
    url: `/life/venue/booking/cancel/${bookingId}`,
    method: 'put'
  })
}

// ==================== 失物招领 ====================

/**
 * 分页查询失物招领
 */
export function getLostFoundList(params?: { type?: number; category?: string; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/life/lost-found/list',
    method: 'get',
    params
  })
}

/**
 * 查询详情
 */
export function getLostFoundDetail(id: number) {
  return request({
    url: `/life/lost-found/${id}`,
    method: 'get'
  })
}

/**
 * 发布失物招领
 */
export function publishLostFound(data: any) {
  return request({
    url: '/life/lost-found',
    method: 'post',
    data
  })
}

/**
 * 标记已解决
 */
export function resolveLostFound(id: number) {
  return request({
    url: `/life/lost-found/resolve/${id}`,
    method: 'put'
  })
}
