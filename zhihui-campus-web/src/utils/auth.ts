import Cookies from 'js-cookie'

const TOKEN_KEY = 'zhihui_token'
const REFRESH_TOKEN_KEY = 'zhihui_refresh_token'

/**
 * 获取Token
 */
export function getToken(): string | undefined {
  return Cookies.get(TOKEN_KEY)
}

/**
 * 设置Token
 */
export function setToken(token: string): void {
  Cookies.set(TOKEN_KEY, token)
}

/**
 * 移除Token
 */
export function removeToken(): void {
  Cookies.remove(TOKEN_KEY)
  Cookies.remove(REFRESH_TOKEN_KEY)
}

/**
 * 获取刷新Token
 */
export function getRefreshToken(): string | undefined {
  return Cookies.get(REFRESH_TOKEN_KEY)
}

/**
 * 设置刷新Token
 */
export function setRefreshToken(token: string): void {
  Cookies.set(REFRESH_TOKEN_KEY, token)
}
