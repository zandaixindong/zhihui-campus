import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi, getUserInfo as getUserInfoApi } from '@/api/auth'
import { setToken, removeToken } from '@/utils/auth'

interface UserInfo {
  userId: number
  username: string
  realName: string
  userType: number
  avatar?: string
  phone?: string
  email?: string
  collegeId?: number
  collegeName?: string
  roles?: string[]
  permissions?: string[]
}

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  /**
   * 用户登录
   */
  async function login(username: string, password: string) {
    const res = await loginApi({ username, password })
    const data = res.data as any
    setToken(data.accessToken)
    return data
  }

  /**
   * 获取用户信息
   */
  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    const data = res.data as any
    userInfo.value = data
    roles.value = data.roles || []
    permissions.value = data.permissions || []
    return data
  }

  /**
   * 退出登录
   */
  async function logout() {
    try {
      await logoutApi()
    } finally {
      resetState()
    }
  }

  /**
   * 重置状态
   */
  function resetState() {
    userInfo.value = null
    roles.value = []
    permissions.value = []
    removeToken()
  }

  /**
   * 检查权限
   */
  function hasPermission(permission: string): boolean {
    if (permissions.value.includes('*:*:*')) {
      return true
    }
    return permissions.value.includes(permission)
  }

  /**
   * 检查角色
   */
  function hasRole(role: string): boolean {
    if (roles.value.includes('admin')) {
      return true
    }
    return roles.value.includes(role)
  }

  return {
    userInfo,
    roles,
    permissions,
    login,
    fetchUserInfo,
    logout,
    resetState,
    hasPermission,
    hasRole
  }
})
