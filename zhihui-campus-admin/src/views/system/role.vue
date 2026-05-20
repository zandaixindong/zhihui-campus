<template>
  <div class="role-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item label="角色名称">
          <el-input v-model="keyword" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button type="success" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="roleList" v-loading="loading" stripe>
        <el-table-column prop="roleId" label="角色ID" width="100" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleKey" label="权限标识" width="150" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" @click="handlePermission(row)">权限</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="roleForm" :rules="roleRules" ref="roleFormRef" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" />
        </el-form-item>
        <el-form-item label="权限标识" prop="roleKey">
          <el-input v-model="roleForm.roleKey" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="roleForm.status">
            <el-radio :value="0">正常</el-radio>
            <el-radio :value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="roleForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog v-model="permDialogVisible" title="分配权限" width="500px">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        show-checkbox
        node-key="menuId"
        :default-checked-keys="checkedMenuIds"
        :props="{ label: 'menuName', children: 'children' }"
      />
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermission">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole, getMenuList, assignPermissions } from '@/api/system'

const loading = ref(false)
const dialogVisible = ref(false)
const permDialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const keyword = ref('')
const roleList = ref<any[]>([])
const menuTree = ref<any[]>([])
const checkedMenuIds = ref<number[]>([])
const currentRoleId = ref(0)
const roleFormRef = ref<FormInstance>()
const menuTreeRef = ref<any>(null)

const roleForm = reactive({
  roleId: undefined as number | undefined,
  roleName: '',
  roleKey: '',
  status: 0,
  remark: ''
})

const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleKey: [{ required: true, message: '请输入权限标识', trigger: 'blur' }]
}

const loadList = async () => {
  loading.value = true
  try {
    const res = await getRoleList({ roleName: keyword.value })
    roleList.value = res.data || []
  } catch {
    roleList.value = []
  } finally {
    loading.value = false
  }
}

const loadMenuTree = async () => {
  try {
    const res = await getMenuList()
    menuTree.value = res.data || []
  } catch {}
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  Object.assign(roleForm, { roleId: undefined, roleName: '', roleKey: '', status: 0, remark: '' })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑角色'
  Object.assign(roleForm, row)
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm(`确认删除角色「${row.roleName}」？`, '提示')
  await deleteRole(row.roleId)
  ElMessage.success('删除成功')
  loadList()
}

const handlePermission = async (row: any) => {
  currentRoleId.value = row.roleId
  checkedMenuIds.value = row.menuIds || []
  permDialogVisible.value = true
}

const handleSavePermission = async () => {
  const checkedKeys = menuTreeRef.value?.getCheckedKeys() || []
  await assignPermissions(currentRoleId.value, checkedKeys)
  ElMessage.success('权限分配成功')
  permDialogVisible.value = false
}

const handleSubmit = async () => {
  await roleFormRef.value?.validate()
  if (roleForm.roleId) {
    await updateRole(roleForm)
    ElMessage.success('更新成功')
  } else {
    await addRole(roleForm)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadList()
}

onMounted(() => { loadList(); loadMenuTree() })
</script>

<style scoped>
.role-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
