<template>
  <div class="schedule-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button @click="handlePrevWeek">&lt; 上一周</el-button>
            <span class="week-title">第 {{ currentWeek }} 周</span>
            <el-button @click="handleNextWeek">下一周 &gt;</el-button>
            <el-button type="primary" link @click="handleCurrentWeek">回到本周</el-button>
          </div>
          <el-select v-model="currentWeek" @change="loadSchedule" style="width: 120px;">
            <el-option v-for="w in 20" :key="w" :label="`第${w}周`" :value="w" />
          </el-select>
        </div>
      </template>

      <div class="schedule-table" v-loading="loading">
        <table>
          <thead>
            <tr>
              <th class="time-header">节次</th>
              <th v-for="day in weekDays" :key="day" :class="{ 'today': isToday(day) }">
                {{ day }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(section, sIndex) in sections" :key="sIndex">
              <td class="time-cell">
                <div class="section-name">{{ section.name }}</div>
                <div class="section-time">{{ section.time }}</div>
              </td>
              <td v-for="(day, dIndex) in 5" :key="dIndex"
                  :class="{ 'has-course': getCourse(day, section.id) }"
                  @click="handleCellClick(day, section.id)">
                <div v-if="getCourse(day, section.id)" class="course-card">
                  <div class="course-name">{{ getCourse(day, section.id).courseName }}</div>
                  <div class="course-info">{{ getCourse(day, section.id).teacherName }}</div>
                  <div class="course-info">{{ getCourse(day, section.id).classroom }}</div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </el-card>

    <!-- 课程详情 -->
    <el-dialog v-model="detailVisible" title="课程详情" width="400px">
      <el-descriptions :column="1" border v-if="selectedCourse">
        <el-descriptions-item label="课程名称">{{ selectedCourse.courseName }}</el-descriptions-item>
        <el-descriptions-item label="授课教师">{{ selectedCourse.teacherName }}</el-descriptions-item>
        <el-descriptions-item label="教室">{{ selectedCourse.classroom }}</el-descriptions-item>
        <el-descriptions-item label="周次">{{ selectedCourse.weekRange }}</el-descriptions-item>
        <el-descriptions-item label="节次">{{ selectedCourse.sectionRange }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentSchedule, getWeekSchedule } from '@/api/edu'

const loading = ref(false)
const currentWeek = ref(1)
const scheduleData = ref<any[]>([])
const detailVisible = ref(false)
const selectedCourse = ref<any>(null)

const weekDays = ['周一', '周二', '周三', '周四', '周五']
const sections = [
  { id: 1, name: '第1-2节', time: '08:00-09:40' },
  { id: 2, name: '第3-4节', time: '10:00-11:40' },
  { id: 3, name: '第5-6节', time: '14:00-15:40' },
  { id: 4, name: '第7-8节', time: '16:00-17:40' },
  { id: 5, name: '第9-10节', time: '19:00-20:40' }
]

const isToday = (day: string) => {
  const today = new Date().getDay()
  const dayMap: Record<string, number> = { '周一': 1, '周二': 2, '周三': 3, '周四': 4, '周五': 5 }
  return dayMap[day] === today
}

const getCourse = (day: number, sectionId: number) => {
  return scheduleData.value.find(item => item.dayOfWeek === day && item.sectionId === sectionId)
}

const loadSchedule = async () => {
  loading.value = true
  try {
    const res = await getWeekSchedule(currentWeek.value)
    scheduleData.value = res.data || []
  } catch {
    scheduleData.value = []
  } finally {
    loading.value = false
  }
}

const handlePrevWeek = () => {
  if (currentWeek.value > 1) {
    currentWeek.value--
    loadSchedule()
  }
}

const handleNextWeek = () => {
  if (currentWeek.value < 20) {
    currentWeek.value++
    loadSchedule()
  }
}

const handleCurrentWeek = () => {
  currentWeek.value = 1
  loadSchedule()
}

const handleCellClick = (day: number, sectionId: number) => {
  const course = getCourse(day, sectionId)
  if (course) {
    selectedCourse.value = course
    detailVisible.value = true
  }
}

onMounted(() => loadSchedule())
</script>

<style scoped>
.schedule-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.header-left { display: flex; align-items: center; gap: 12px; }
.week-title { font-size: 16px; font-weight: bold; min-width: 60px; text-align: center; }
.schedule-table { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
th, td { border: 1px solid #ebeef5; padding: 8px; text-align: center; min-width: 120px; height: 100px; vertical-align: top; }
th { background: #f5f7fa; font-weight: 600; }
th.today { background: #ecf5ff; color: #409eff; }
.time-header { width: 100px; }
.time-cell { background: #f5f7fa; }
.section-name { font-weight: bold; font-size: 13px; }
.section-time { font-size: 11px; color: #909399; margin-top: 4px; }
td.has-course { cursor: pointer; }
td.has-course:hover { background: #f0f9ff; }
.course-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 6px; padding: 8px; font-size: 12px; height: 100%; display: flex; flex-direction: column; justify-content: center; }
.course-name { font-weight: bold; font-size: 13px; margin-bottom: 4px; }
.course-info { opacity: 0.9; margin-top: 2px; }
</style>
