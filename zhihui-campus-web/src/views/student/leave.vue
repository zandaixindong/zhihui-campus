<template>
  <div class="leave-container">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="审批状态">
          <el-select v-model="queryParams.status" placeholder="全部状态" clearable>
            <el-option label="待审批" :value="0" />
            <el-option label="辅导员通过" :value="1" />
            <el-option label="辅导员拒绝" :value="2" />
            <el-option label="学院通过" :value="3" />
            <el-option label="学院拒绝" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="success" @click="handleAdd">申请请假</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 请假记录列表 -->
    <el-card class="table-card">
      <el-table :data="leaveList" v-loading="loading" stripe>
        <el-table-column prop="leaveType" label="请假类型" width="100">
          <template #default="{ row }">
            <el-tag :type="leaveTypeTag(row.leaveType)">{{ leaveTypeText(row.leaveType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="days" label="天数" width="80" align="center" />
        <el-table-column prop="reason" label="请假原因" show-overflow-tooltip />
        <el-table-column prop="status" label="审批状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-if="row.status === 0">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadLeaveList"
      />
    </el-card>

    <!-- 申请请假对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="leaveForm" :rules="rules" ref="leaveFormRef" label-width="100px">
        <el-form-item label="请假类型" prop="leaveType">
          <el-radio-group v-model="leaveForm.leaveType">
            <el-radio :value="1">事假</el-radio>
            <el-radio :value="2">病假</el-radio>
            <el-radio :value="3">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="leaveForm.startDate" type="date" placeholder="选择开始日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="leaveForm.endDate" type="date" placeholder="选择结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="leaveForm.reason" type="textarea" :rows="4" placeholder="请输入请假原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="请假详情" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="请假类型">{{ leaveTypeText(detailData.leaveType) }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="请假天数">{{ detailData.days }}天</el-descriptions-item>
        <el-descriptions-item label="请假原因">{{ detailData.reason }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag :type="statusTag(detailData.status)">{{ statusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="辅导员审批" v-if="detailData.status >= 1">
          {{ detailData.counselorRemark || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="学院审批" v-if="detailData.status >= 3">
          {{ detailData.collegeRemark || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getLeaveList, submitLeave, cancelLeave } from '@/api/student'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const dialogTitle = ref('申请请假')
const leaveFormRef = ref<FormInstance>()
const leaveList = ref<any[]>([])
const detailData = ref<any>({})
const total = ref(0)

const queryParams = reactive({
  status: undefined as number | undefined,
  pageNum: 1,
  pageSize: 10
})

const leaveForm = reactive({
  leaveType: 1,
  startDate: '',
  endDate: '',
  reason: ''
})

const rules = {
  leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  reason: [{ required: true, message: '请输入请假原因', trigger: 'blur' }]
}

const leaveTypeText = (type: number) => ({ 1: '事假', 2: '病假', 3: '其他' }[type] || '未知')
const leaveTypeTag = (type: number) => ({ 1: '', 2: 'warning', 3: 'info' }[type] as any)
const statusText = (status: number) => ({
  0: '待审批', 1: '辅导员通过', 2: '辅导员拒绝', 3: '学院通过', 4: '学院拒绝'
}[status] || '未知')
const statusTag = (status: number) => ({
  0: 'warning', 1: 'success', 2: 'danger', 3: 'success', 4: 'danger'
}[status] as any)

const loadLeaveList = async () => {
  loading.value = true
  try {
    const res = await getLeaveList(queryParams)
    leaveList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  loadLeaveList()
}

const handleAdd = () => {
  dialogTitle.value = '申请请假'
  Object.assign(leaveForm, { leaveType: 1, startDate: '', endDate: '', reason: '' })
  dialogVisible.value = true
}

const handleDetail = (row: any) => {
  detailData.value = row
  detailVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm('确认撤回该请假申请？', '提示')
  await cancelLeave(row.id)
  ElMessage.success('已撤回')
  loadLeaveList()
}

const handleSubmit = async () => {
  await leaveFormRef.value?.validate()
  submitting.value = true
  try {
    await submitLeave(leaveForm)
    ElMessage.success('提交成功')
    dialogVisible.value = false
    loadLeaveList()
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadLeaveList())
</script>

<style scoped>
.leave-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
