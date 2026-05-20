import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'

NProgress.configure({ showSpinner: false })

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/auth/login.vue'),
      meta: { title: '登录', hidden: true }
    },
    {
      path: '/',
      component: () => import('@/layouts/index.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          meta: { title: '首页', icon: 'HomeFilled', affix: true }
        }
      ]
    },
    {
      path: '/edu',
      component: () => import('@/layouts/index.vue'),
      redirect: '/edu/schedule',
      meta: { title: '教务服务', icon: 'Reading' },
      children: [
        {
          path: 'schedule',
          name: 'Schedule',
          component: () => import('@/views/edu/schedule.vue'),
          meta: { title: '我的课表', icon: 'Calendar' }
        },
        {
          path: 'score',
          name: 'Score',
          component: () => import('@/views/edu/score.vue'),
          meta: { title: '成绩查询', icon: 'Document' }
        },
        {
          path: 'exam',
          name: 'Exam',
          component: () => import('@/views/edu/exam.vue'),
          meta: { title: '考试安排', icon: 'Timer' }
        },
        {
          path: 'course',
          name: 'Course',
          component: () => import('@/views/edu/course.vue'),
          meta: { title: '我的课程', icon: 'Notebook' }
        }
      ]
    },
    {
      path: '/student',
      component: () => import('@/layouts/index.vue'),
      redirect: '/student/leave',
      meta: { title: '学工服务', icon: 'UserFilled' },
      children: [
        {
          path: 'leave',
          name: 'Leave',
          component: () => import('@/views/student/leave.vue'),
          meta: { title: '请假申请', icon: 'Calendar' }
        },
        {
          path: 'reward',
          name: 'Reward',
          component: () => import('@/views/student/reward.vue'),
          meta: { title: '奖惩记录', icon: 'Trophy' }
        },
        {
          path: 'work-study',
          name: 'WorkStudy',
          component: () => import('@/views/student/work-study.vue'),
          meta: { title: '勤工助学', icon: 'Briefcase' }
        },
        {
          path: 'counseling',
          name: 'Counseling',
          component: () => import('@/views/student/counseling.vue'),
          meta: { title: '心理咨询', icon: 'FirstAidKit' }
        }
      ]
    },
    {
      path: '/life',
      component: () => import('@/layouts/index.vue'),
      redirect: '/life/card',
      meta: { title: '生活服务', icon: 'Coffee' },
      children: [
        {
          path: 'card',
          name: 'Card',
          component: () => import('@/views/life/card.vue'),
          meta: { title: '一卡通', icon: 'CreditCard' }
        },
        {
          path: 'repair',
          name: 'Repair',
          component: () => import('@/views/life/repair.vue'),
          meta: { title: '报修服务', icon: 'SetUp' }
        },
        {
          path: 'booking',
          name: 'Booking',
          component: () => import('@/views/life/booking.vue'),
          meta: { title: '场地预约', icon: 'Location' }
        },
        {
          path: 'lost-found',
          name: 'LostFound',
          component: () => import('@/views/life/lost-found.vue'),
          meta: { title: '失物招领', icon: 'Search' }
        }
      ]
    },
    {
      path: '/notice',
      component: () => import('@/layouts/index.vue'),
      redirect: '/notice/list',
      meta: { title: '通知公告', icon: 'Bell' },
      children: [
        {
          path: 'list',
          name: 'NoticeList',
          component: () => import('@/views/notice/list.vue'),
          meta: { title: '公告列表', icon: 'List' }
        },
        {
          path: 'detail/:id',
          name: 'NoticeDetail',
          component: () => import('@/views/notice/detail.vue'),
          meta: { title: '公告详情', hidden: true }
        },
        {
          path: 'message',
          name: 'Message',
          component: () => import('@/views/notice/message.vue'),
          meta: { title: '我的消息', icon: 'Message' }
        }
      ]
    },
    {
      path: '/community',
      component: () => import('@/layouts/index.vue'),
      redirect: '/community/post',
      meta: { title: '校园社区', icon: 'ChatDotRound' },
      children: [
        {
          path: 'post',
          name: 'Post',
          component: () => import('@/views/community/post.vue'),
          meta: { title: '校园论坛', icon: 'ChatLineRound' }
        },
        {
          path: 'post/:id',
          name: 'PostDetail',
          component: () => import('@/views/community/post-detail.vue'),
          meta: { title: '帖子详情', hidden: true }
        }
      ]
    },
    {
      path: '/profile',
      component: () => import('@/layouts/index.vue'),
      children: [
        {
          path: '',
          name: 'Profile',
          component: () => import('@/views/profile/index.vue'),
          meta: { title: '个人中心', icon: 'User' }
        }
      ]
    },
    {
      path: '/404',
      name: 'NotFound',
      component: () => import('@/views/error/404.vue'),
      meta: { title: '404', hidden: true }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404'
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  NProgress.start()
  document.title = `${to.meta.title || ''} - 智慧校园`

  const token = getToken()

  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router
