<template>
  <div class="work-study-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item>
          <el-button type="success" @click="handleApply">申请岗位</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="岗位列表" name="list">
          <el-table :data="jobList" v-loading="loading" stripe>
            <el-table-column prop="jobName" label="岗位名称" />
            <el-table-column prop="collegeName" label="所属学院" width="150" />
            <el-table-column prop="salary" label="薪资(元/月)" width="120" align="center" />
            <el-table-column prop="headcount" label="招聘人数" width="100" align="center" />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '招聘中' : '已结束' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleApplyJob(row)" :disabled="row.status !== 1">申请</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="我的申请" name="mine">
          <el-table :data="myApplications" v-loading="loadingMine" stripe>
            <el-table-column prop="jobName" label="岗位名称" />
            <el-table-column prop="status" label="状态" width="120" align="center">
              <template #default="{ row }">
                <el-tag :type="applicationStatusTag(row.status)">{{ applicationStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="applyTime" label="申请时间" width="170" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button link type="danger" @click="handleCancel(row)" v-if="row.status === 0">取消</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getWorkStudyPositions, submitWorkStudyApplication, getWorkStudyApplications } from '@/api/student'

const activeTab = ref('list')
const loading = ref(false)
const loadingMine = ref(false)
const jobList = ref<any[]>([])
const myApplications = ref<any[]>([])

const applicationStatusText = (s: number) => ({ 0: '待审批', 1: '已通过', 2: '已拒绝', 3: '已取消' }[s] || '未知')
const applicationStatusTag = (s: number) => ({ 0: 'warning', 1: 'success', 2: 'danger', 3: 'info' }[s] as any)

const loadJobs = async () => {
  loading.value = true
  try {
    const res = await getWorkStudyPositions({ pageNum: 1, pageSize: 50 })
    jobList.value = res.data.records
  } finally {
    loading.value = false
  }
}

const loadMyApplications = async () => {
  loadingMine.value = true
  try {
    const res = await getWorkStudyApplications()
    myApplications.value = res.data
  } finally {
    loadingMine.value = false
  }
}

const handleApply = () => { activeTab.value = 'list' }
const handleApplyJob = async (row: any) => {
  await ElMessageBox.confirm(`确认申请岗位「${row.jobName}」？`, '提示')
  await submitWorkStudyApplication({ workStudyId: row.id })
  ElMessage.success('申请已提交')
  loadMyApplications()
}
const handleCancel = async (row: any) => {
  await ElMessageBox.confirm('确认取消申请？', '提示')
  await cancelWorkStudyApplication(row.id)
  ElMessage.success('已取消')
  loadMyApplications()
}

onMounted(() => { loadJobs(); loadMyApplications() })
</script>

<style scoped>
.work-study-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
