import request from '@/utils/request'

/**
 * 用户登录
 */
export function login(data: { username: string; password: string }) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户登出
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 获取当前用户信息
 */
export function getUserInfo() {
  return request({
    url: '/auth/user-info',
    method: 'get'
  })
}

/**
 * 刷新Token
 */
export function refreshToken(refreshToken: string) {
  return request({
    url: '/auth/refresh',
    method: 'post',
    params: { refreshToken }
  })
}

/**
 * 修改密码
 */
export function changePassword(data: { userId: number; oldPassword: string; newPassword: string }) {
  return request({
    url: '/auth/password',
    method: 'put',
    params: data
  })
}

/**
 * 用户注册
 */
export function register(data: { username: string; password: string }) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}
