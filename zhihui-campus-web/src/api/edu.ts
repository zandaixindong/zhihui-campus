import request from '@/utils/request'

/**
 * 获取当前学期课表
 */
export function getCurrentSchedule() {
  return request({
    url: '/edu/schedule/current',
    method: 'get'
  })
}

/**
 * 获取指定周课表
 */
export function getWeekSchedule(week: number) {
  return request({
    url: `/edu/schedule/week/${week}`,
    method: 'get'
  })
}

/**
 * 获取成绩列表
 */
export function getScoreList(params?: { semesterId?: number }) {
  return request({
    url: '/edu/score/list',
    method: 'get',
    params
  })
}

/**
 * 获取GPA统计
 */
export function getGpaStats() {
  return request({
    url: '/edu/score/gpa',
    method: 'get'
  })
}

/**
 * 获取考试安排
 */
export function getExamList(params?: { semesterId?: number }) {
  return request({
    url: '/edu/exam/list',
    method: 'get',
    params
  })
}

/**
 * 获取已选课程
 */
export function getSelectedCourses(params?: { semesterId?: number }) {
  return request({
    url: '/edu/course/selected',
    method: 'get',
    params
  })
}

/**
 * 提交教学评价
 */
export function submitEvaluation(data: { courseOfferId: number; score: number; content: string }) {
  return request({
    url: '/edu/evaluation/submit',
    method: 'post',
    data
  })
}

/**
 * 获取学期列表
 */
export function getSemesterList() {
  return request({
    url: '/edu/semester/list',
    method: 'get'
  })
}
