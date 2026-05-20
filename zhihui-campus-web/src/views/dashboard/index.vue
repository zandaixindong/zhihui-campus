<template>
  <div class="dashboard-container">
    <!-- 欢迎信息 -->
    <div class="welcome-section card">
      <div class="welcome-info">
        <h2>欢迎回来，{{ userInfo?.realName || '同学' }}！</h2>
        <p>{{ greeting }}，今天是 {{ currentDate }}</p>
      </div>
      <div class="welcome-stats">
        <div class="stat-item">
          <span class="stat-value">第 {{ currentWeek }} 周</span>
          <span class="stat-label">当前周次</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ currentSemester }}</span>
          <span class="stat-label">当前学期</span>
        </div>
      </div>
    </div>

    <!-- 功能入口 -->
    <div class="entry-section">
      <el-row :gutter="20">
        <el-col :span="6" v-for="entry in entryList" :key="entry.path">
          <div class="entry-card card" @click="router.push(entry.path)">
            <el-icon :size="40" :color="entry.color">
              <component :is="entry.icon" />
            </el-icon>
            <h3>{{ entry.title }}</h3>
            <p>{{ entry.desc }}</p>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 今日课表 -->
    <div class="schedule-section card">
      <div class="section-header">
        <h3>今日课表</h3>
        <el-link type="primary" @click="router.push('/edu/schedule')">查看全部</el-link>
      </div>
      <div v-if="todaySchedule.length > 0" class="schedule-list">
        <div v-for="item in todaySchedule" :key="item.id" class="schedule-item">
          <div class="schedule-time">
            <span class="section">{{ item.startSection }}-{{ item.endSection }}节</span>
          </div>
          <div class="schedule-info">
            <h4>{{ item.courseName }}</h4>
            <p>
              <el-icon><Location /></el-icon>
              {{ item.classroom }}
            </p>
          </div>
        </div>
      </div>
      <el-empty v-else description="今天没有课" :image-size="100" />
    </div>

    <!-- 最新公告 -->
    <div class="notice-section card">
      <div class="section-header">
        <h3>最新公告</h3>
        <el-link type="primary" @click="router.push('/notice/list')">查看全部</el-link>
      </div>
      <div v-if="noticeList.length > 0" class="notice-list">
        <div
          v-for="notice in noticeList"
          :key="notice.id"
          class="notice-item"
          @click="router.push(`/notice/detail/${notice.id}`)"
        >
          <div class="notice-title">
            <el-tag v-if="notice.isTop" type="danger" size="small">置顶</el-tag>
            <span>{{ notice.title }}</span>
          </div>
          <span class="notice-time">{{ notice.publishTime }}</span>
        </div>
      </div>
      <el-empty v-else description="暂无公告" :image-size="100" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import dayjs from 'dayjs'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

// 当前日期
const currentDate = computed(() => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

// 当前周次
const currentWeek = ref(1)

// 当前学期
const currentSemester = ref('2024-2025-2')

// 问候语
const greeting = computed(() => {
  const hour = dayjs().hour()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  return '晚上好'
})

// 功能入口
const entryList = [
  {
    title: '我的课表',
    desc: '查看课程安排',
    icon: 'Calendar',
    color: '#409eff',
    path: '/edu/schedule'
  },
  {
    title: '成绩查询',
    desc: '查看考试成绩',
    icon: 'Document',
    color: '#67c23a',
    path: '/edu/score'
  },
  {
    title: '一卡通',
    desc: '余额消费查询',
    icon: 'CreditCard',
    color: '#e6a23c',
    path: '/life/card'
  },
  {
    title: '报修服务',
    desc: '在线报修',
    icon: 'SetUp',
    color: '#f56c6c',
    path: '/life/repair'
  }
]

// 今日课表
const todaySchedule = ref<any[]>([])

// 公告列表
const noticeList = ref<any[]>([])

// 获取数据
const fetchData = async () => {
  try {
    // TODO: 调用接口获取今日课表和公告
    // const scheduleRes = await getCurrentSchedule()
    // todaySchedule.value = scheduleRes.data
    // const noticeRes = await getNoticeList()
    // noticeList.value = noticeRes.data
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  .welcome-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;

    h2 {
      margin: 0 0 8px;
      font-size: 24px;
    }

    p {
      margin: 0;
      opacity: 0.8;
    }

    .welcome-stats {
      display: flex;
      gap: 30px;
    }

    .stat-item {
      text-align: center;

      .stat-value {
        display: block;
        font-size: 20px;
        font-weight: 600;
      }

      .stat-label {
        font-size: 14px;
        opacity: 0.8;
      }
    }
  }

  .entry-section {
    margin-bottom: 20px;

    .entry-card {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s;
      padding: 24px;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
      }

      h3 {
        margin: 12px 0 8px;
        font-size: 16px;
      }

      p {
        margin: 0;
        font-size: 14px;
        color: #999;
      }
    }
  }

  .schedule-section,
  .notice-section {
    margin-bottom: 20px;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        margin: 0;
        font-size: 18px;
      }
    }
  }

  .schedule-list {
    .schedule-item {
      display: flex;
      padding: 12px 0;
      border-bottom: 1px solid #eee;

      &:last-child {
        border-bottom: none;
      }

      .schedule-time {
        width: 80px;
        text-align: center;

        .section {
          font-size: 14px;
          color: #409eff;
        }
      }

      .schedule-info {
        flex: 1;

        h4 {
          margin: 0 0 4px;
          font-size: 16px;
        }

        p {
          margin: 0;
          font-size: 14px;
          color: #666;
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }

  .notice-list {
    .notice-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #eee;
      cursor: pointer;

      &:last-child {
        border-bottom: none;
      }

      &:hover {
        color: #409eff;
      }

      .notice-title {
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .notice-time {
        font-size: 14px;
        color: #999;
      }
    }
  }
}
</style>
