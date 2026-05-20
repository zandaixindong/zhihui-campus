<template>
  <div class="notice-container">
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="公告标题">
          <el-input v-model="queryParams.title" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="公告类型">
          <el-select v-model="queryParams.noticeType" placeholder="全部" clearable>
            <el-option label="学校公告" :value="1" />
            <el-option label="学院通知" :value="2" />
            <el-option label="教务通知" :value="3" />
            <el-option label="学工通知" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="success" @click="handleAdd">发布公告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="noticeList" v-loading="loading" stripe>
        <el-table-column prop="noticeId" label="ID" width="80" />
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="noticeType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="noticeTypeTag(row.noticeType)">{{ noticeTypeText(row.noticeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publisher" label="发布人" width="100" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'info'">{{ row.status === 0 ? '已发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="阅读量" width="80" align="center" />
        <el-table-column prop="createTime" label="发布时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @change="loadList"
        style="margin-top: 16px; justify-content: flex-end;"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" top="5vh">
      <el-form :model="noticeForm" :rules="noticeRules" ref="noticeFormRef" label-width="100px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="noticeForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="公告类型" prop="noticeType">
          <el-select v-model="noticeForm.noticeType" placeholder="请选择">
            <el-option label="学校公告" :value="1" />
            <el-option label="学院通知" :value="2" />
            <el-option label="教务通知" :value="3" />
            <el-option label="学工通知" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布状态">
          <el-radio-group v-model="noticeForm.status">
            <el-radio :value="0">立即发布</el-radio>
            <el-radio :value="1">存为草稿</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input v-model="noticeForm.content" type="textarea" :rows="10" placeholder="请输入公告内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('发布公告')
const noticeList = ref<any[]>([])
const total = ref(0)
const noticeFormRef = ref<FormInstance>()

const queryParams = reactive({ title: '', noticeType: undefined as number | undefined, pageNum: 1, pageSize: 10 })

const noticeForm = reactive({
  noticeId: undefined as number | undefined,
  title: '',
  noticeType: 1,
  content: '',
  status: 0
})

const noticeRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  noticeType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const noticeTypeText = (t: number) => ({ 1: '学校公告', 2: '学院通知', 3: '教务通知', 4: '学工通知' }[t] || '其他')
const noticeTypeTag = (t: number) => ({ 1: 'danger', 2: 'warning', 3: '', 4: 'success' }[t] as any) || 'info'

const loadList = async () => {
  loading.value = true
  try {
    // 模拟数据
    noticeList.value = [
      { noticeId: 1, title: '关于2024年春季学期开学的通知', noticeType: 1, publisher: '教务处', status: 0, viewCount: 1256, createTime: '2024-03-01 10:00' },
      { noticeId: 2, title: '图书馆开放时间调整通知', noticeType: 2, publisher: '图书馆', status: 0, viewCount: 856, createTime: '2024-03-05 14:30' }
    ]
    total.value = 2
  } finally { loading.value = false }
}

const handleQuery = () => { queryParams.pageNum = 1; loadList() }

const handleAdd = () => {
  Object.assign(noticeForm, { noticeId: undefined, title: '', noticeType: 1, content: '', status: 0 })
  dialogTitle.value = '发布公告'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  Object.assign(noticeForm, row)
  dialogTitle.value = '编辑公告'
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm(`确认删除公告「${row.title}」？`, '提示')
  ElMessage.success('删除成功')
  loadList()
}

const handleSubmit = async () => {
  await noticeFormRef.value?.validate()
  ElMessage.success(noticeForm.noticeId ? '更新成功' : '发布成功')
  dialogVisible.value = false
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.notice-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
