<template>
  <div class="log-container">
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="操作日志" name="oper" />
        <el-tab-pane label="登录日志" name="login" />
      </el-tabs>

      <el-form :model="queryParams" inline style="margin-bottom: 16px;">
        <el-form-item label="操作人" v-if="activeTab === 'oper'">
          <el-input v-model="queryParams.operName" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="用户名" v-if="activeTab === 'login'">
          <el-input v-model="queryParams.username" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable>
            <el-option label="成功" :value="0" />
            <el-option label="失败" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作日志表格 -->
      <el-table v-if="activeTab === 'oper'" :data="operLogList" v-loading="loading" stripe>
        <el-table-column prop="operId" label="日志ID" width="80" />
        <el-table-column prop="operName" label="操作人" width="100" />
        <el-table-column prop="operType" label="操作类型" width="100" />
        <el-table-column prop="method" label="请求方法" width="80" />
        <el-table-column prop="url" label="请求URL" width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '成功' : '失败' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operTime" label="操作时间" width="170" />
      </el-table>

      <!-- 登录日志表格 -->
      <el-table v-if="activeTab === 'login'" :data="loginLogList" v-loading="loading" stripe>
        <el-table-column prop="infoId" label="日志ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column prop="browser" label="浏览器" width="120" />
        <el-table-column prop="os" label="操作系统" width="120" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '成功' : '失败' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="msg" label="提示消息" />
        <el-table-column prop="loginTime" label="登录时间" width="170" />
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getOperLogList, getLoginLogList } from '@/api/system'

const loading = ref(false)
const activeTab = ref('oper')
const operLogList = ref<any[]>([])
const loginLogList = ref<any[]>([])
const total = ref(0)

const queryParams = reactive({
  operName: '',
  username: '',
  status: undefined as number | undefined,
  pageNum: 1,
  pageSize: 10
})

const handleTabChange = () => { queryParams.pageNum = 1; loadList() }

const loadList = async () => {
  loading.value = true
  try {
    if (activeTab.value === 'oper') {
      const res = await getOperLogList(queryParams)
      operLogList.value = res.data?.records || []
      total.value = res.data?.total || 0
    } else {
      const res = await getLoginLogList(queryParams)
      loginLogList.value = res.data?.records || []
      total.value = res.data?.total || 0
    }
  } catch {
    operLogList.value = []
    loginLogList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => loadList())
</script>

<style scoped>
.log-container { padding: 20px; }
</style>
