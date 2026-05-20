<template>
  <div class="booking-container">
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="场地类型">
          <el-select v-model="queryParams.venueType" placeholder="全部" clearable>
            <el-option label="教室" :value="1" />
            <el-option label="会议室" :value="2" />
            <el-option label="体育场馆" :value="3" />
            <el-option label="活动室" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询场地</el-button>
          <el-button type="success" @click="handleMyBookings">我的预约</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 场地列表 -->
    <el-card v-if="!showMyBookings">
      <el-table :data="venueList" v-loading="loading" stripe>
        <el-table-column prop="name" label="场地名称" />
        <el-table-column prop="venueType" label="类型" width="100">
          <template #default="{ row }">{{ venueTypeText(row.venueType) }}</template>
        </el-table-column>
        <el-table-column prop="location" label="位置" width="150" />
        <el-table-column prop="capacity" label="容量" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '可用' : '维护中' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleBook(row)" :disabled="row.status !== 1">预约</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 我的预约 -->
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <span>我的预约</span>
          <el-button link @click="showMyBookings = false">返回场地列表</el-button>
        </div>
      </template>
      <el-table :data="myBookings" v-loading="loadingBookings" stripe>
        <el-table-column prop="venueName" label="场地" />
        <el-table-column prop="bookingDate" label="预约日期" width="120" />
        <el-table-column prop="timeSlot" label="时间段" width="120" />
        <el-table-column prop="purpose" label="用途" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="bookingStatusTag(row.status)">{{ bookingStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleCancelBooking(row)" v-if="row.status === 0 || row.status === 1">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 预约对话框 -->
    <el-dialog v-model="bookDialogVisible" title="场地预约" width="500px">
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="100px">
        <el-form-item label="场地">{{ bookForm.venueName }}</el-form-item>
        <el-form-item label="预约日期" prop="bookingDate">
          <el-date-picker v-model="bookForm.bookingDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="时间段" prop="timeSlot">
          <el-select v-model="bookForm.timeSlot" placeholder="请选择">
            <el-option label="08:00-10:00" value="08:00-10:00" />
            <el-option label="10:00-12:00" value="10:00-12:00" />
            <el-option label="14:00-16:00" value="14:00-16:00" />
            <el-option label="16:00-18:00" value="16:00-18:00" />
            <el-option label="19:00-21:00" value="19:00-21:00" />
          </el-select>
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="bookForm.purpose" placeholder="如：社团活动、班级会议" />
        </el-form-item>
        <el-form-item label="人数">
          <el-input-number v-model="bookForm.attendeeCount" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bookDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitBooking">确认预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getVenueList, submitVenueBooking, getVenueBookings, cancelVenueBooking } from '@/api/life'

const loading = ref(false)
const loadingBookings = ref(false)
const showMyBookings = ref(false)
const bookDialogVisible = ref(false)
const venueList = ref<any[]>([])
const myBookings = ref<any[]>([])
const bookFormRef = ref<FormInstance>()

const queryParams = reactive({ venueType: undefined as number | undefined })

const bookForm = reactive({
  venueId: 0,
  venueName: '',
  bookingDate: '',
  timeSlot: '',
  purpose: '',
  attendeeCount: 1
})

const bookRules = {
  bookingDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  timeSlot: [{ required: true, message: '请选择时间段', trigger: 'change' }],
  purpose: [{ required: true, message: '请输入用途', trigger: 'blur' }]
}

const venueTypeText = (t: number) => ({ 1: '教室', 2: '会议室', 3: '体育场馆', 4: '活动室' }[t] || '未知')
const bookingStatusText = (s: number) => ({ 0: '待审批', 1: '已通过', 2: '已拒绝', 3: '已取消' }[s] || '未知')
const bookingStatusTag = (s: number) => ({ 0: 'warning', 1: 'success', 2: 'danger', 3: 'info' }[s] as any)

const loadVenues = async () => {
  loading.value = true
  try {
    const res = await getVenueList(queryParams)
    venueList.value = res.data
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { showMyBookings.value = false; loadVenues() }

const handleMyBookings = async () => {
  showMyBookings.value = true
  loadingBookings.value = true
  try {
    const res = await getVenueBookings()
    myBookings.value = res.data
  } finally {
    loadingBookings.value = false
  }
}

const handleBook = (row: any) => {
  bookForm.venueId = row.id
  bookForm.venueName = row.name
  bookForm.bookingDate = ''
  bookForm.timeSlot = ''
  bookForm.purpose = ''
  bookForm.attendeeCount = 1
  bookDialogVisible.value = true
}

const handleSubmitBooking = async () => {
  await bookFormRef.value?.validate()
  await submitVenueBooking(bookForm)
  ElMessage.success('预约成功')
  bookDialogVisible.value = false
}

const handleCancelBooking = async (row: any) => {
  await ElMessageBox.confirm('确认取消预约？', '提示')
  await cancelVenueBooking(row.id)
  ElMessage.success('已取消')
  handleMyBookings()
}

onMounted(() => loadVenues())
</script>

<style scoped>
.booking-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
