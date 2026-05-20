import request from '@/utils/request'

// ==================== 用户管理 ====================
export function getUserList(params?: any) {
  return request({ url: '/system/user/list', method: 'get', params })
}

export function getUserDetail(userId: number) {
  return request({ url: `/system/user/${userId}`, method: 'get' })
}

export function addUser(data: any) {
  return request({ url: '/system/user', method: 'post', data })
}

export function updateUser(data: any) {
  return request({ url: '/system/user', method: 'put', data })
}

export function deleteUser(userId: number) {
  return request({ url: `/system/user/${userId}`, method: 'delete' })
}

export function resetPassword(userId: number, newPassword: string) {
  return request({ url: `/system/user/reset-password`, method: 'put', params: { userId, newPassword } })
}

// ==================== 角色管理 ====================
export function getRoleList(params?: any) {
  return request({ url: '/system/role/list', method: 'get', params })
}

export function addRole(data: any) {
  return request({ url: '/system/role', method: 'post', data })
}

export function updateRole(data: any) {
  return request({ url: '/system/role', method: 'put', data })
}

export function deleteRole(roleId: number) {
  return request({ url: `/system/role/${roleId}`, method: 'delete' })
}

export function getRolePermissions(roleId: number) {
  return request({ url: `/system/role/permissions/${roleId}`, method: 'get' })
}

export function assignPermissions(roleId: number, menuIds: number[]) {
  return request({ url: `/system/role/permissions`, method: 'put', data: { roleId, menuIds } })
}

// ==================== 菜单管理 ====================
export function getMenuList() {
  return request({ url: '/system/menu/list', method: 'get' })
}

export function addMenu(data: any) {
  return request({ url: '/system/menu', method: 'post', data })
}

export function updateMenu(data: any) {
  return request({ url: '/system/menu', method: 'put', data })
}

export function deleteMenu(menuId: number) {
  return request({ url: `/system/menu/${menuId}`, method: 'delete' })
}

// ==================== 字典管理 ====================
export function getDictTypeList(params?: any) {
  return request({ url: '/system/dict/type/list', method: 'get', params })
}

export function addDictType(data: any) {
  return request({ url: '/system/dict/type', method: 'post', data })
}

export function updateDictType(data: any) {
  return request({ url: '/system/dict/type', method: 'put', data })
}

export function deleteDictType(dictId: number) {
  return request({ url: `/system/dict/type/${dictId}`, method: 'delete' })
}

export function getDictDataList(dictType: string) {
  return request({ url: '/system/dict/data/list', method: 'get', params: { dictType } })
}

export function addDictData(data: any) {
  return request({ url: '/system/dict/data', method: 'post', data })
}

export function updateDictData(data: any) {
  return request({ url: '/system/dict/data', method: 'put', data })
}

export function deleteDictData(dictDataId: number) {
  return request({ url: `/system/dict/data/${dictDataId}`, method: 'delete' })
}

// ==================== 学院专业 ====================
export function getCollegeList() {
  return request({ url: '/system/college/list', method: 'get' })
}

export function getMajorList(collegeId?: number) {
  return request({ url: '/system/major/list', method: 'get', params: { collegeId } })
}

// ==================== 日志管理 ====================
export function getOperLogList(params?: any) {
  return request({ url: '/system/log/oper/list', method: 'get', params })
}

export function getLoginLogList(params?: any) {
  return request({ url: '/system/log/login/list', method: 'get', params })
}
