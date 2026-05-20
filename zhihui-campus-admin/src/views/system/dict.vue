<template>
  <div class="dict-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item label="字典名称">
          <el-input v-model="keyword" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button type="success" @click="handleAddType">新增字典</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="dictTypeList" v-loading="loading" stripe>
        <el-table-column prop="dictId" label="字典ID" width="100" />
        <el-table-column prop="dictName" label="字典名称" width="200" />
        <el-table-column prop="dictType" label="字典类型" width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="handleViewData(row)">{{ row.dictType }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEditType(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDeleteType(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 字典数据对话框 -->
    <el-dialog v-model="dataDialogVisible" :title="`字典数据 - ${currentDictType}`" width="800px">
      <el-button type="success" @click="handleAddData" style="margin-bottom: 16px;">新增数据</el-button>
      <el-table :data="dictDataList" v-loading="loadingData" stripe>
        <el-table-column prop="dictLabel" label="字典标签" />
        <el-table-column prop="dictValue" label="字典值" />
        <el-table-column prop="dictSort" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEditData(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDeleteData(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 字典类型表单 -->
    <el-dialog v-model="typeDialogVisible" :title="typeDialogTitle" width="500px">
      <el-form :model="typeForm" label-width="100px">
        <el-form-item label="字典名称" required>
          <el-input v-model="typeForm.dictName" />
        </el-form-item>
        <el-form-item label="字典类型" required>
          <el-input v-model="typeForm.dictType" :disabled="!!typeForm.dictId" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="typeForm.status">
            <el-radio :value="0">正常</el-radio>
            <el-radio :value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="typeForm.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitType">确定</el-button>
      </template>
    </el-dialog>

    <!-- 字典数据表单 -->
    <el-dialog v-model="dataFormVisible" :title="dataFormTitle" width="500px">
      <el-form :model="dataForm" label-width="100px">
        <el-form-item label="字典标签" required>
          <el-input v-model="dataForm.dictLabel" />
        </el-form-item>
        <el-form-item label="字典值" required>
          <el-input v-model="dataForm.dictValue" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="dataForm.dictSort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dataForm.status">
            <el-radio :value="0">正常</el-radio>
            <el-radio :value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitData">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDictTypeList, addDictType, updateDictType, deleteDictType, getDictDataList, addDictData, updateDictData, deleteDictData } from '@/api/system'

const loading = ref(false)
const loadingData = ref(false)
const keyword = ref('')
const dictTypeList = ref<any[]>([])
const dictDataList = ref<any[]>([])
const currentDictType = ref('')
const typeDialogVisible = ref(false)
const typeDialogTitle = ref('新增字典')
const dataDialogVisible = ref(false)
const dataFormVisible = ref(false)
const dataFormTitle = ref('新增数据')

const typeForm = reactive({ dictId: undefined as number | undefined, dictName: '', dictType: '', status: 0, remark: '' })
const dataForm = reactive({ dictCode: undefined as number | undefined, dictType: '', dictLabel: '', dictValue: '', dictSort: 0, status: 0 })

const loadList = async () => {
  loading.value = true
  try {
    const res = await getDictTypeList({ dictName: keyword.value })
    dictTypeList.value = res.data || []
  } catch { dictTypeList.value = [] } finally { loading.value = false }
}

const handleAddType = () => {
  Object.assign(typeForm, { dictId: undefined, dictName: '', dictType: '', status: 0, remark: '' })
  typeDialogTitle.value = '新增字典'
  typeDialogVisible.value = true
}

const handleEditType = (row: any) => {
  Object.assign(typeForm, row)
  typeDialogTitle.value = '编辑字典'
  typeDialogVisible.value = true
}

const handleDeleteType = async (row: any) => {
  await ElMessageBox.confirm(`确认删除字典「${row.dictName}」？`, '提示')
  await deleteDictType(row.dictId)
  ElMessage.success('删除成功')
  loadList()
}

const handleSubmitType = async () => {
  if (typeForm.dictId) await updateDictType(typeForm)
  else await addDictType(typeForm)
  ElMessage.success(typeForm.dictId ? '更新成功' : '新增成功')
  typeDialogVisible.value = false
  loadList()
}

const handleViewData = async (row: any) => {
  currentDictType.value = row.dictType
  dataDialogVisible.value = true
  loadingData.value = true
  try {
    const res = await getDictDataList(row.dictType)
    dictDataList.value = res.data || []
  } catch { dictDataList.value = [] } finally { loadingData.value = false }
}

const handleAddData = () => {
  Object.assign(dataForm, { dictCode: undefined, dictType: currentDictType.value, dictLabel: '', dictValue: '', dictSort: 0, status: 0 })
  dataFormTitle.value = '新增数据'
  dataFormVisible.value = true
}

const handleEditData = (row: any) => {
  Object.assign(dataForm, row)
  dataFormTitle.value = '编辑数据'
  dataFormVisible.value = true
}

const handleDeleteData = async (row: any) => {
  await ElMessageBox.confirm('确认删除该字典数据？', '提示')
  await deleteDictData(row.dictCode)
  ElMessage.success('删除成功')
  handleViewData({ dictType: currentDictType.value })
}

const handleSubmitData = async () => {
  if (dataForm.dictCode) await updateDictData(dataForm)
  else await addDictData(dataForm)
  ElMessage.success(dataForm.dictCode ? '更新成功' : '新增成功')
  dataFormVisible.value = false
  handleViewData({ dictType: currentDictType.value })
}

onMounted(() => loadList())
</script>

<style scoped>
.dict-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
