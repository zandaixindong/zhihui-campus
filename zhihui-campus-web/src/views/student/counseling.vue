<template>
  <div class="counseling-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item>
          <el-button type="success" @click="handleBook">预约咨询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="appointmentList" v-loading="loading" stripe>
        <el-table-column prop="counselorName" label="咨询师" width="120" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" />
        <el-table-column prop="timeSlot" label="时间段" width="120" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleCancel(row)" v-if="row.status === 0">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadList"
      />
    </el-card>

    <!-- 预约对话框 -->
    <el-dialog v-model="bookVisible" title="预约心理咨询" width="500px">
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="100px">
        <el-form-item label="选择咨询师" prop="counselorId">
          <el-select v-model="bookForm.counselorId" placeholder="请选择">
            <el-option v-for="c in counselors" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约日期" prop="appointmentDate">
          <el-date-picker v-model="bookForm.appointmentDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="时间段" prop="timeSlot">
          <el-select v-model="bookForm.timeSlot" placeholder="请选择">
            <el-option label="09:00-10:00" value="09:00-10:00" />
            <el-option label="10:00-11:00" value="10:00-11:00" />
            <el-option label="14:00-15:00" value="14:00-15:00" />
            <el-option label="15:00-16:00" value="15:00-16:00" />
            <el-option label="16:00-17:00" value="16:00-17:00" />
          </el-select>
        </el-form-item>
        <el-form-item label="咨询类型" prop="type">
          <el-select v-model="bookForm.type" placeholder="请选择">
            <el-option label="个人咨询" :value="1" />
            <el-option label="团体咨询" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="bookForm.remark" type="textarea" :rows="3" placeholder="简要描述咨询需求" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bookVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getCounselingAppointments, submitCounselingAppointment, cancelCounselingAppointment } from '@/api/student'

const loading = ref(false)
const bookVisible = ref(false)
const appointmentList = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const bookFormRef = ref<FormInstance>()

const counselors = ref([
  { id: 1, name: '张老师' },
  { id: 2, name: '李老师' },
  { id: 3, name: '王老师' }
])

const bookForm = reactive({
  counselorId: undefined as number | undefined,
  appointmentDate: '',
  timeSlot: '',
  type: 1,
  remark: ''
})

const bookRules = {
  counselorId: [{ required: true, message: '请选择咨询师', trigger: 'change' }],
  appointmentDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  timeSlot: [{ required: true, message: '请选择时间段', trigger: 'change' }],
  type: [{ required: true, message: '请选择咨询类型', trigger: 'change' }]
}

const statusText = (s: number) => ({ 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消', 4: '已拒绝' }[s] || '未知')
const statusTag = (s: number) => ({ 0: 'warning', 1: 'success', 2: '', 3: 'info', 4: 'danger' }[s] as any)

const loadList = async () => {
  loading.value = true
  try {
    const res = await getCounselingAppointments({ pageNum: pageNum.value, pageSize: pageSize.value })
    appointmentList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleBook = () => {
  Object.assign(bookForm, { counselorId: undefined, appointmentDate: '', timeSlot: '', type: 1, remark: '' })
  bookVisible.value = true
}

const handleSubmit = async () => {
  await bookFormRef.value?.validate()
  await submitCounselingAppointment(bookForm)
  ElMessage.success('预约成功，等待咨询师确认')
  bookVisible.value = false
  loadList()
}

const handleCancel = async (row: any) => {
  await ElMessageBox.confirm('确认取消预约？', '提示')
  await cancelCounselingAppointment(row.id)
  ElMessage.success('已取消')
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.counseling-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
