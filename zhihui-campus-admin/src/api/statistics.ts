import request from '@/utils/request'

export function getDashboardStats() {
  return request({ url: '/system/stats/dashboard', method: 'get' })
}

export function getUserStats() {
  return request({ url: '/system/stats/users', method: 'get' })
}

export function getVisitStats(params?: { startDate?: string; endDate?: string }) {
  return request({ url: '/system/stats/visits', method: 'get', params })
}

export function getFunctionStats() {
  return request({ url: '/system/stats/functions', method: 'get' })
}
