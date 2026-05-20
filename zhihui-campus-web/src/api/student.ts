import request from '@/utils/request'

// ==================== 请假管理 ====================

/**
 * 分页查询请假申请
 */
export function getLeaveList(params?: { studentId?: number; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/student/leave/list',
    method: 'get',
    params
  })
}

/**
 * 查询请假详情
 */
export function getLeaveDetail(leaveId: number) {
  return request({
    url: `/student/leave/${leaveId}`,
    method: 'get'
  })
}

/**
 * 提交请假申请
 */
export function submitLeave(data: any) {
  return request({
    url: '/student/leave',
    method: 'post',
    data
  })
}

/**
 * 辅导员审批
 */
export function counselorApprove(leaveId: number, approved: boolean, remark?: string) {
  return request({
    url: `/student/leave/counselor/approve/${leaveId}`,
    method: 'put',
    params: { approved, remark }
  })
}

/**
 * 学院审批
 */
export function collegeApprove(leaveId: number, approverId: number, approved: boolean, remark?: string) {
  return request({
    url: `/student/leave/college/approve/${leaveId}`,
    method: 'put',
    params: { approverId, approved, remark }
  })
}

/**
 * 取消请假申请
 */
export function cancelLeave(leaveId: number) {
  return request({
    url: `/student/leave/${leaveId}`,
    method: 'delete'
  })
}

// ==================== 奖惩记录 ====================

/**
 * 分页查询奖惩记录
 */
export function getRewardPunishmentList(params?: { studentId?: number; type?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/student/reward-punishment/list',
    method: 'get',
    params
  })
}

// ==================== 勤工助学 ====================

/**
 * 分页查询岗位列表
 */
export function getWorkStudyPositions(params?: { positionType?: string; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/student/work-study/positions',
    method: 'get',
    params
  })
}

/**
 * 查询岗位详情
 */
export function getWorkStudyDetail(positionId: number) {
  return request({
    url: `/student/work-study/positions/${positionId}`,
    method: 'get'
  })
}

/**
 * 分页查询申请记录
 */
export function getWorkStudyApplications(params?: { studentId?: number; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/student/work-study/applications',
    method: 'get',
    params
  })
}

/**
 * 提交岗位申请
 */
export function submitWorkStudyApplication(data: any) {
  return request({
    url: '/student/work-study/applications',
    method: 'post',
    data
  })
}

/**
 * 取消岗位申请
 */
export function cancelWorkStudyApplication(applicationId: number) {
  return request({
    url: `/student/work-study/applications/${applicationId}`,
    method: 'delete'
  })
}

// ==================== 心理咨询 ====================

/**
 * 分页查询预约记录
 */
export function getCounselingAppointments(params?: { studentId?: number; status?: number; pageNum?: number; pageSize?: number }) {
  return request({
    url: '/student/counseling/appointments',
    method: 'get',
    params
  })
}

/**
 * 查询可用时间段
 */
export function getCounselingSlots(counselorId: number, date: string) {
  return request({
    url: '/student/counseling/available-slots',
    method: 'get',
    params: { counselorId, date }
  })
}

/**
 * 提交预约申请
 */
export function submitCounselingAppointment(data: any) {
  return request({
    url: '/student/counseling/appointments',
    method: 'post',
    data
  })
}

/**
 * 取消预约
 */
export function cancelCounselingAppointment(appointmentId: number) {
  return request({
    url: `/student/counseling/appointments/cancel/${appointmentId}`,
    method: 'put'
  })
}
