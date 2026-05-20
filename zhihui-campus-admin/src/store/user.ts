import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi, getUserInfo as getUserInfoApi } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(getToken() || '')
  const userInfo = ref<any>({})

  async function login(username: string, password: string) {
    const res = await loginApi({ username, password })
    token.value = res.data.token
    setToken(res.data.token)
    return res
  }

  async function getUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res.data
    return res
  }

  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = {}
      removeToken()
    }
  }

  return { token, userInfo, login, getUserInfo, logout }
})
