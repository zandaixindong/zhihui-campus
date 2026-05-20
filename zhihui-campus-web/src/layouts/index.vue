<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container" :class="{ collapse: isCollapse }">
      <div class="logo-container">
        <img src="@/assets/images/logo.svg" alt="Logo" class="logo" />
        <span v-if="!isCollapse" class="title">智慧校园</span>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :collapse-transition="false"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
          router
        >
          <template v-for="route in menuRoutes" :key="route.path">
            <!-- 单级菜单 -->
            <el-menu-item
              v-if="!route.children || route.children.length === 1"
              :index="getMenuPath(route)"
            >
              <el-icon v-if="getMenuIcon(route)">
                <component :is="getMenuIcon(route)" />
              </el-icon>
              <template #title>{{ getMenuTitle(route) }}</template>
            </el-menu-item>

            <!-- 多级菜单 -->
            <el-sub-menu v-else :index="route.path">
              <template #title>
                <el-icon v-if="route.meta?.icon">
                  <component :is="route.meta.icon" />
                </el-icon>
                <span>{{ route.meta?.title }}</span>
              </template>
              <el-menu-item
                v-for="child in route.children"
                :key="child.path"
                :index="`${route.path}/${child.path}`"
              >
                <el-icon v-if="child.meta?.icon">
                  <component :is="child.meta.icon" />
                </el-icon>
                <template #title>{{ child.meta?.title }}</template>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-scrollbar>
    </div>

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 头部 -->
      <div class="navbar">
        <div class="flex-between">
          <div class="flex-center">
            <el-icon class="collapse-btn" @click="toggleSidebar">
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
                {{ item.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="flex-center">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="mr-10">
              <el-icon :size="20" class="cursor-pointer" @click="goToMessage">
                <Bell />
              </el-icon>
            </el-badge>
            <el-dropdown trigger="click" @command="handleCommand">
              <div class="flex-center cursor-pointer">
                <el-avatar :size="32" :src="userInfo?.avatar" icon="UserFilled" />
                <span class="ml-10">{{ userInfo?.realName || userInfo?.username }}</span>
                <el-icon class="ml-10"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="password">
                    <el-icon><Lock /></el-icon>修改密码
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <!-- 内容区 -->
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <keep-alive :include="cachedViews">
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const unreadCount = ref(0)
const cachedViews = ref<string[]>([])

// 用户信息
const userInfo = computed(() => userStore.userInfo)

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta?.activeMenu) {
    return meta.activeMenu as string
  }
  return path
})

// 面包屑
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta?.title)
  return matched.map(item => ({
    path: item.path,
    title: item.meta?.title as string
  }))
})

// 菜单路由
const menuRoutes = computed(() => {
  return router.options.routes.filter(route => {
    return !route.meta?.hidden && route.path !== '/login' && route.path !== '/404'
  })
})

// 获取菜单路径
const getMenuPath = (route: any) => {
  if (route.children && route.children.length === 1) {
    return `${route.path}/${route.children[0].path}`
  }
  return route.path
}

// 获取菜单图标
const getMenuIcon = (route: any) => {
  if (route.children && route.children.length === 1) {
    return route.children[0].meta?.icon
  }
  return route.meta?.icon
}

// 获取菜单标题
const getMenuTitle = (route: any) => {
  if (route.children && route.children.length === 1) {
    return route.children[0].meta?.title
  }
  return route.meta?.title
}

// 切换侧边栏
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 跳转消息页
const goToMessage = () => {
  router.push('/notice/message')
}

// 下拉菜单命令处理
const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'password':
      // 打开修改密码对话框
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logout()
    router.push('/login')
    ElMessage.success('退出成功')
  })
}

// 初始化
const init = async () => {
  if (!userStore.userInfo) {
    await userStore.fetchUserInfo()
  }
}

init()
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  height: 100%;
  width: 100%;
}

.sidebar-container {
  width: var(--sidebar-width);
  height: 100%;
  background-color: var(--sidebar-bg-color);
  transition: width 0.3s;
  overflow: hidden;

  &.collapse {
    width: var(--sidebar-collapse-width);
  }

  .logo-container {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px;
    background-color: #2b2f3a;

    .logo {
      width: 32px;
      height: 32px;
    }

    .title {
      color: #fff;
      font-size: 16px;
      font-weight: 600;
      margin-left: 10px;
      white-space: nowrap;
    }
  }

  .el-menu {
    border-right: none;
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.navbar {
  height: var(--header-height);
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
  display: flex;
  align-items: center;

  .collapse-btn {
    font-size: 20px;
    cursor: pointer;
    margin-right: 15px;
  }

  .cursor-pointer {
    cursor: pointer;
  }
}

.app-main {
  flex: 1;
  padding: 20px;
  background-color: var(--bg-color);
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
