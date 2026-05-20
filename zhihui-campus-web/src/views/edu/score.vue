<template>
  <div class="score-container">
    <!-- GPA统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ gpaStats.gpa || '--' }}</div>
          <div class="stat-label">总GPA</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ gpaStats.avgScore || '--' }}</div>
          <div class="stat-label">平均分</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ gpaStats.totalCredits || '--' }}</div>
          <div class="stat-label">总学分</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ gpaStats.courseCount || '--' }}</div>
          <div class="stat-label">课程数</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>成绩列表</span>
          <el-select v-model="semesterId" placeholder="选择学期" clearable @change="loadScores" style="width: 200px;">
            <el-option v-for="s in semesters" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </div>
      </template>
      <el-table :data="scoreList" v-loading="loading" stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="courseCode" label="课程代码" width="120" />
        <el-table-column prop="credits" label="学分" width="80" align="center" />
        <el-table-column prop="score" label="成绩" width="80" align="center">
          <template #default="{ row }">
            <span :class="{ 'fail': row.score < 60 }">{{ row.score }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gradePoint" label="绩点" width="80" align="center" />
        <el-table-column prop="examType" label="考试类型" width="100" align="center" />
        <el-table-column prop="semester" label="学期" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getScoreList, getGpaStats, getSemesterList } from '@/api/edu'

const loading = ref(false)
const scoreList = ref<any[]>([])
const semesters = ref<any[]>([])
const semesterId = ref<number | undefined>(undefined)
const gpaStats = ref<any>({})

const loadScores = async () => {
  loading.value = true
  try {
    const res = await getScoreList({ semesterId: semesterId.value })
    scoreList.value = res.data || []
  } finally {
    loading.value = false
  }
}

const loadGpaStats = async () => {
  try {
    const res = await getGpaStats()
    gpaStats.value = res.data || {}
  } catch {}
}

const loadSemesters = async () => {
  try {
    const res = await getSemesterList()
    semesters.value = res.data || []
  } catch {}
}

onMounted(() => {
  loadScores()
  loadGpaStats()
  loadSemesters()
})
</script>

<style scoped>
.score-container { padding: 20px; }
.stats-row { margin-bottom: 20px; }
.stat-card { text-align: center; }
.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.fail { color: #f56c6c; font-weight: bold; }
</style>
