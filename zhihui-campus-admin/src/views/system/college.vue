<template>
  <div class="college-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学院管理</span>
          <el-button type="success" @click="handleAdd">新增学院</el-button>
        </div>
      </template>
      <el-table :data="collegeList" v-loading="loading" stripe>
        <el-table-column prop="collegeId" label="学院ID" width="100" />
        <el-table-column prop="collegeName" label="学院名称" />
        <el-table-column prop="collegeCode" label="学院代码" width="120" />
        <el-table-column prop="majorCount" label="专业数" width="100" align="center" />
        <el-table-column prop="studentCount" label="学生数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleViewMajors(row)">专业</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 专业列表对话框 -->
    <el-dialog v-model="majorDialogVisible" :title="`专业管理 - ${currentCollege}`" width="800px">
      <el-button type="success" @click="handleAddMajor" style="margin-bottom: 16px;">新增专业</el-button>
      <el-table :data="majorList" v-loading="loadingMajor" stripe>
        <el-table-column prop="majorId" label="专业ID" width="100" />
        <el-table-column prop="majorName" label="专业名称" />
        <el-table-column prop="majorCode" label="专业代码" width="120" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEditMajor(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDeleteMajor(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 新增/编辑学院 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="学院名称" required>
          <el-input v-model="form.collegeName" />
        </el-form-item>
        <el-form-item label="学院代码" required>
          <el-input v-model="form.collegeCode" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">正常</el-radio>
            <el-radio :value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增/编辑专业 -->
    <el-dialog v-model="majorFormVisible" :title="majorFormTitle" width="500px">
      <el-form :model="majorForm" label-width="100px">
        <el-form-item label="专业名称" required>
          <el-input v-model="majorForm.majorName" />
        </el-form-item>
        <el-form-item label="专业代码" required>
          <el-input v-model="majorForm.majorCode" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="majorFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitMajor">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCollegeList, getMajorList } from '@/api/system'

const loading = ref(false)
const loadingMajor = ref(false)
const dialogVisible = ref(false)
const majorDialogVisible = ref(false)
const majorFormVisible = ref(false)
const dialogTitle = ref('新增学院')
const majorFormTitle = ref('新增专业')
const currentCollege = ref('')
const currentCollegeId = ref(0)
const collegeList = ref<any[]>([])
const majorList = ref<any[]>([])

const form = reactive({ collegeId: undefined as number | undefined, collegeName: '', collegeCode: '', status: 0 })
const majorForm = reactive({ majorId: undefined as number | undefined, collegeId: 0, majorName: '', majorCode: '' })

const loadList = async () => {
  loading.value = true
  try {
    const res = await getCollegeList()
    collegeList.value = res.data || []
  } catch { collegeList.value = [] } finally { loading.value = false }
}

const handleAdd = () => {
  Object.assign(form, { collegeId: undefined, collegeName: '', collegeCode: '', status: 0 })
  dialogTitle.value = '新增学院'
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑学院'
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm(`确认删除学院「${row.collegeName}」？`, '提示')
  ElMessage.success('删除成功')
  loadList()
}

const handleSubmit = () => {
  ElMessage.success(form.collegeId ? '更新成功' : '新增成功')
  dialogVisible.value = false
  loadList()
}

const handleViewMajors = async (row: any) => {
  currentCollege.value = row.collegeName
  currentCollegeId.value = row.collegeId
  majorDialogVisible.value = true
  loadingMajor.value = true
  try {
    const res = await getMajorList(row.collegeId)
    majorList.value = res.data || []
  } catch { majorList.value = [] } finally { loadingMajor.value = false }
}

const handleAddMajor = () => {
  Object.assign(majorForm, { majorId: undefined, collegeId: currentCollegeId.value, majorName: '', majorCode: '' })
  majorFormTitle.value = '新增专业'
  majorFormVisible.value = true
}

const handleEditMajor = (row: any) => {
  Object.assign(majorForm, row)
  majorFormTitle.value = '编辑专业'
  majorFormVisible.value = true
}

const handleDeleteMajor = async (row: any) => {
  await ElMessageBox.confirm(`确认删除专业「${row.majorName}」？`, '提示')
  ElMessage.success('删除成功')
  handleViewMajors({ collegeId: currentCollegeId.value, collegeName: currentCollege.value })
}

const handleSubmitMajor = () => {
  ElMessage.success(majorForm.majorId ? '更新成功' : '新增成功')
  majorFormVisible.value = false
  handleViewMajors({ collegeId: currentCollegeId.value, collegeName: currentCollege.value })
}

onMounted(() => loadList())
</script>

<style scoped>
.college-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
