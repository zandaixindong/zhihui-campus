<template>
  <div class="repair-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item>
          <el-button type="success" @click="handleAdd">提交报修</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="repairList" v-loading="loading" stripe>
        <el-table-column prop="orderNo" label="工单号" width="140" />
        <el-table-column prop="category" label="报修类型" width="100">
          <template #default="{ row }">
            {{ categoryText(row.category) }}
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置" width="150" />
        <el-table-column prop="description" label="问题描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
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

    <!-- 提交报修对话框 -->
    <el-dialog v-model="dialogVisible" title="提交报修" width="600px">
      <el-form :model="repairForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="报修类型" prop="category">
          <el-select v-model="repairForm.category" placeholder="请选择">
            <el-option label="水电维修" :value="1" />
            <el-option label="家具维修" :value="2" />
            <el-option label="网络故障" :value="3" />
            <el-option label="空调维修" :value="4" />
            <el-option label="其他" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="报修位置" prop="location">
          <el-input v-model="repairForm.location" placeholder="如：东区6号楼305室" />
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="repairForm.description" type="textarea" :rows="4" placeholder="请详细描述问题" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="repairForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="期望时间">
          <el-date-picker v-model="repairForm.expectTime" type="datetime" placeholder="选择期望维修时间" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="报修详情" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="工单号">{{ detailData.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="报修类型">{{ categoryText(detailData.category) }}</el-descriptions-item>
        <el-descriptions-item label="位置">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="问题描述">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTag(detailData.status)">{{ statusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人" v-if="detailData.handlerName">{{ detailData.handlerName }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" v-if="detailData.handleResult">{{ detailData.handleResult }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getRepairList, submitRepair, cancelRepair } from '@/api/life'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const repairList = ref<any[]>([])
const detailData = ref<any>({})
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const formRef = ref<FormInstance>()

const repairForm = reactive({
  category: undefined as number | undefined,
  location: '',
  description: '',
  phone: '',
  expectTime: ''
})

const rules = {
  category: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  location: [{ required: true, message: '请输入报修位置', trigger: 'blur' }],
  description: [{ required: true, message: '请描述问题', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

const categoryText = (c: number) => ({ 1: '水电维修', 2: '家具维修', 3: '网络故障', 4: '空调维修', 5: '其他' }[c] || '未知')
const statusText = (s: number) => ({ 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }[s] || '未知')
const statusTag = (s: number) => ({ 0: 'warning', 1: '', 2: 'success', 3: 'info' }[s] as any)

const loadList = async () => {
  loading.value = true
  try {
    const res = await getRepairList({ pageNum: pageNum.value, pageSize: pageSize.value })
    repairList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  Object.assign(repairForm, { category: undefined, location: '', description: '', phone: '', expectTime: '' })
  dialogVisible.value = true
}

const handleDetail = (row: any) => { detailData.value = row; detailVisible.value = true }

const handleCancel = async (row: any) => {
  await ElMessageBox.confirm('确认取消该报修工单？', '提示')
  await cancelRepair(row.id)
  ElMessage.success('已取消')
  loadList()
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  submitting.value = true
  try {
    await submitRepair(repairForm)
    ElMessage.success('报修已提交')
    dialogVisible.value = false
    loadList()
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadList())
</script>

<style scoped>
.repair-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
