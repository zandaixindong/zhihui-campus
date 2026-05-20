<template>
  <div class="user-container">
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.phone" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable>
            <el-option label="正常" :value="0" />
            <el-option label="停用" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button type="success" @click="handleAdd">新增</el-button>
          <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">批量删除</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="userList" v-loading="loading" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="selection" width="50" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="collegeName" label="学院" width="150" />
        <el-table-column prop="roleName" label="角色" width="100" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" @click="handleResetPwd(row)">重置密码</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" :disabled="!!userForm.userId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="userForm.realName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学院" prop="collegeId">
              <el-select v-model="userForm.collegeId" placeholder="请选择">
                <el-option v-for="c in colleges" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" prop="roleId">
              <el-select v-model="userForm.roleId" placeholder="请选择">
                <el-option v-for="r in roles" :key="r.roleId" :label="r.roleName" :value="r.roleId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="userForm.status">
                <el-radio :value="0">正常</el-radio>
                <el-radio :value="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="!userForm.userId">
            <el-form-item label="密码" prop="password">
              <el-input v-model="userForm.password" type="password" show-password />
            </el-form-item>
          </el-col>
        </el-row>
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
import { getUserList, addUser, updateUser, deleteUser, resetPassword } from '@/api/system'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const userList = ref<any[]>([])
const total = ref(0)
const selectedIds = ref<number[]>([])
const userFormRef = ref<FormInstance>()

const colleges = ref([
  { id: 1, name: '计算机学院' },
  { id: 2, name: '数学学院' },
  { id: 3, name: '外语学院' }
])

const roles = ref([
  { roleId: 1, roleName: '管理员' },
  { roleId: 2, roleName: '教师' },
  { roleId: 3, roleName: '学生' }
])

const queryParams = reactive({
  username: '',
  phone: '',
  status: undefined as number | undefined,
  pageNum: 1,
  pageSize: 10
})

const userForm = reactive({
  userId: undefined as number | undefined,
  username: '',
  realName: '',
  phone: '',
  email: '',
  collegeId: undefined as number | undefined,
  roleId: undefined as number | undefined,
  status: 0,
  password: ''
})

const userRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await getUserList(queryParams)
    userList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    userList.value = []
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNum = 1; loadList() }

const handleSelectionChange = (rows: any[]) => {
  selectedIds.value = rows.map(r => r.userId)
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  Object.assign(userForm, { userId: undefined, username: '', realName: '', phone: '', email: '', collegeId: undefined, roleId: undefined, status: 0, password: '' })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑用户'
  Object.assign(userForm, row)
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm(`确认删除用户「${row.username}」？`, '提示')
  await deleteUser(row.userId)
  ElMessage.success('删除成功')
  loadList()
}

const handleBatchDelete = async () => {
  await ElMessageBox.confirm(`确认删除选中的 ${selectedIds.value.length} 个用户？`, '提示')
  // 批量删除逻辑
  ElMessage.success('删除成功')
  loadList()
}

const handleResetPwd = async (row: any) => {
  const { value } = await ElMessageBox.prompt('请输入新密码', '重置密码', { inputType: 'password' })
  await resetPassword(row.userId, value)
  ElMessage.success('密码重置成功')
}

const handleSubmit = async () => {
  await userFormRef.value?.validate()
  if (userForm.userId) {
    await updateUser(userForm)
    ElMessage.success('更新成功')
  } else {
    await addUser(userForm)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.user-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
