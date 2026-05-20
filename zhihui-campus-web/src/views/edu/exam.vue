<template>
  <div class="exam-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试安排</span>
          <el-select v-model="semesterId" placeholder="选择学期" clearable @change="loadExams" style="width: 200px;">
            <el-option v-for="s in semesters" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </div>
      </template>
      <el-table :data="examList" v-loading="loading" stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="examType" label="考试类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.examType === '期末' ? 'danger' : ''">{{ row.examType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="examDate" label="考试日期" width="120" />
        <el-table-column prop="examTime" label="考试时间" width="120" />
        <el-table-column prop="classroom" label="考场" width="120" />
        <el-table-column prop="seatNo" label="座位号" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="examStatusTag(row.status)">{{ examStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && examList.length === 0" description="暂无考试安排" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getExamList, getSemesterList } from '@/api/edu'

const loading = ref(false)
const examList = ref<any[]>([])
const semesters = ref<any[]>([])
const semesterId = ref<number | undefined>(undefined)

const examStatusText = (s: string) => ({ upcoming: '未开始', ongoing: '进行中', finished: '已结束' }[s] || s)
const examStatusTag = (s: string) => ({ upcoming: 'warning', ongoing: 'danger', finished: 'info' }[s] as any) || ''

const loadExams = async () => {
  loading.value = true
  try {
    const res = await getExamList({ semesterId: semesterId.value })
    examList.value = res.data || []
  } finally {
    loading.value = false
  }
}

const loadSemesters = async () => {
  try {
    const res = await getSemesterList()
    semesters.value = res.data || []
  } catch {}
}

onMounted(() => { loadExams(); loadSemesters() })
</script>

<style scoped>
.exam-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
