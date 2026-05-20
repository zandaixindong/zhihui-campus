<template>
  <div class="lost-found-container">
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="类型">
          <el-select v-model="queryParams.type" placeholder="全部" clearable>
            <el-option label="寻物启事" :value="1" />
            <el-option label="失物招领" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="success" @click="handlePublish">发布信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16">
      <el-col :span="8" v-for="item in list" :key="item.id">
        <el-card class="item-card" shadow="hover">
          <div class="item-header">
            <el-tag :type="item.type === 1 ? 'danger' : 'success'" size="small">
              {{ item.type === 1 ? '寻物' : '招领' }}
            </el-tag>
            <span class="item-time">{{ item.createTime }}</span>
          </div>
          <div class="item-title">{{ item.title }}</div>
          <div class="item-desc">{{ item.description }}</div>
          <div class="item-info">
            <span><el-icon><Location /></el-icon> {{ item.location }}</span>
          </div>
          <div class="item-contact">联系人：{{ item.contactName }} / {{ item.contactPhone }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next"
      @change="loadList"
      style="margin-top: 20px; justify-content: center;"
    />

    <!-- 发布对话框 -->
    <el-dialog v-model="publishVisible" title="发布信息" width="600px">
      <el-form :model="publishForm" :rules="publishRules" ref="publishFormRef" label-width="100px">
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="publishForm.type">
            <el-radio :value="1">寻物启事</el-radio>
            <el-radio :value="2">失物招领</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="publishForm.title" placeholder="简要描述物品" />
        </el-form-item>
        <el-form-item label="详细描述" prop="description">
          <el-input v-model="publishForm.description" type="textarea" :rows="4" placeholder="物品特征、丢失/拾取经过等" />
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="publishForm.location" placeholder="丢失/拾取地点" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="publishForm.contactName" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="publishForm.contactPhone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="publishVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitPublish">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
import { getLostFoundList, publishLostFound } from '@/api/life'

const loading = ref(false)
const publishVisible = ref(false)
const list = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(9)
const publishFormRef = ref<FormInstance>()

const queryParams = reactive({ type: undefined as number | undefined })

const publishForm = reactive({
  type: 1,
  title: '',
  description: '',
  location: '',
  contactName: '',
  contactPhone: ''
})

const publishRules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }],
  location: [{ required: true, message: '请输入地点', trigger: 'blur' }],
  contactName: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入电话', trigger: 'blur' }]
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await getLostFoundList({ ...queryParams, pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { pageNum.value = 1; loadList() }

const handlePublish = () => {
  Object.assign(publishForm, { type: 1, title: '', description: '', location: '', contactName: '', contactPhone: '' })
  publishVisible.value = true
}

const handleSubmitPublish = async () => {
  await publishFormRef.value?.validate()
  await publishLostFound(publishForm)
  ElMessage.success('发布成功')
  publishVisible.value = false
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.lost-found-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
.item-card { margin-bottom: 16px; }
.item-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.item-time { font-size: 12px; color: #909399; }
.item-title { font-size: 16px; font-weight: bold; margin-bottom: 8px; }
.item-desc { font-size: 14px; color: #606266; margin-bottom: 10px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.item-info { font-size: 13px; color: #909399; margin-bottom: 8px; }
.item-contact { font-size: 13px; color: #409eff; }
</style>
