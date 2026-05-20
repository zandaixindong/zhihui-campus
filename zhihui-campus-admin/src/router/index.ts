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
      component: () => import('@/views/login/index.vue'),
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
      path: '/system',
      component: () => import('@/layouts/index.vue'),
      redirect: '/system/user',
      meta: { title: '系统管理', icon: 'Setting' },
      children: [
        {
          path: 'user',
          name: 'User',
          component: () => import('@/views/system/user.vue'),
          meta: { title: '用户管理', icon: 'User' }
        },
        {
          path: 'role',
          name: 'Role',
          component: () => import('@/views/system/role.vue'),
          meta: { title: '角色管理', icon: 'UserFilled' }
        },
        {
          path: 'menu',
          name: 'Menu',
          component: () => import('@/views/system/menu.vue'),
          meta: { title: '菜单管理', icon: 'Menu' }
        },
        {
          path: 'dict',
          name: 'Dict',
          component: () => import('@/views/system/dict.vue'),
          meta: { title: '字典管理', icon: 'Collection' }
        },
        {
          path: 'college',
          name: 'College',
          component: () => import('@/views/system/college.vue'),
          meta: { title: '学院管理', icon: 'School' }
        },
        {
          path: 'log',
          name: 'Log',
          component: () => import('@/views/system/log.vue'),
          meta: { title: '日志管理', icon: 'Document' }
        }
      ]
    },
    {
      path: '/content',
      component: () => import('@/layouts/index.vue'),
      redirect: '/content/notice',
      meta: { title: '内容管理', icon: 'Document' },
      children: [
        {
          path: 'notice',
          name: 'Notice',
          component: () => import('@/views/content/notice.vue'),
          meta: { title: '公告管理', icon: 'Bell' }
        }
      ]
    },
    {
      path: '/monitor',
      component: () => import('@/layouts/index.vue'),
      redirect: '/monitor/server',
      meta: { title: '系统监控', icon: 'Monitor' },
      children: [
        {
          path: 'server',
          name: 'Server',
          component: () => import('@/views/monitor/server.vue'),
          meta: { title: '服务器监控', icon: 'Cpu' }
        },
        {
          path: 'online',
          name: 'Online',
          component: () => import('@/views/monitor/online.vue'),
          meta: { title: '在线用户', icon: 'Connection' }
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
  document.title = `${to.meta.title || ''} - 智慧校园管理后台`

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
