<template>
  <div class="menu-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item label="菜单名称">
          <el-input v-model="keyword" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button type="success" @click="handleAdd()">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="menuTree" v-loading="loading" row-key="menuId" :tree-props="{ children: 'children' }" default-expand-all>
        <el-table-column prop="menuName" label="菜单名称" width="200" />
        <el-table-column prop="icon" label="图标" width="100" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column prop="path" label="路由地址" width="150" />
        <el-table-column prop="component" label="组件路径" width="180" />
        <el-table-column prop="menuType" label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="menuTypeTag(row.menuType)">{{ menuTypeText(row.menuType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleAdd(row.menuId)" v-if="row.menuType !== 'F'">新增</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="menuForm" :rules="menuRules" ref="menuFormRef" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select v-model="menuForm.parentId" :data="menuOptions" :props="{ label: 'menuName', value: 'menuId', children: 'children' }" placeholder="选择上级菜单" check-strictly clearable />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="menuForm.menuType">
            <el-radio value="M">目录</el-radio>
            <el-radio value="C">菜单</el-radio>
            <el-radio value="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" />
        </el-form-item>
        <el-form-item label="图标" v-if="menuForm.menuType !== 'F'">
          <el-input v-model="menuForm.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="menuForm.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="路由地址" v-if="menuForm.menuType !== 'F'" prop="path">
          <el-input v-model="menuForm.path" />
        </el-form-item>
        <el-form-item label="组件路径" v-if="menuForm.menuType === 'C'">
          <el-input v-model="menuForm.component" />
        </el-form-item>
        <el-form-item label="权限标识" v-if="menuForm.menuType === 'F'">
          <el-input v-model="menuForm.perms" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="menuForm.status">
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getMenuList, addMenu, updateMenu, deleteMenu } from '@/api/system'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const keyword = ref('')
const menuTree = ref<any[]>([])
const menuOptions = ref<any[]>([])
const menuFormRef = ref<FormInstance>()

const menuForm = reactive({
  menuId: undefined as number | undefined,
  parentId: 0,
  menuType: 'M',
  menuName: '',
  icon: '',
  orderNum: 0,
  path: '',
  component: '',
  perms: '',
  status: 0
})

const menuRules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  orderNum: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  path: [{ required: true, message: '请输入路由地址', trigger: 'blur' }]
}

const menuTypeText = (t: string) => ({ M: '目录', C: '菜单', F: '按钮' }[t] || '未知')
const menuTypeTag = (t: string) => ({ M: '', C: 'success', F: 'warning' }[t] as any) || 'info'

const loadList = async () => {
  loading.value = true
  try {
    const res = await getMenuList()
    menuTree.value = res.data || []
    menuOptions.value = [{ menuId: 0, menuName: '根菜单', children: res.data || [] }]
  } catch {
    menuTree.value = []
  } finally {
    loading.value = false
  }
}

const handleAdd = (parentId?: number) => {
  dialogTitle.value = '新增菜单'
  Object.assign(menuForm, { menuId: undefined, parentId: parentId || 0, menuType: 'M', menuName: '', icon: '', orderNum: 0, path: '', component: '', perms: '', status: 0 })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑菜单'
  Object.assign(menuForm, row)
  dialogVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm(`确认删除菜单「${row.menuName}」？`, '提示')
  await deleteMenu(row.menuId)
  ElMessage.success('删除成功')
  loadList()
}

const handleSubmit = async () => {
  await menuFormRef.value?.validate()
  if (menuForm.menuId) {
    await updateMenu(menuForm)
    ElMessage.success('更新成功')
  } else {
    await addMenu(menuForm)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.menu-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
