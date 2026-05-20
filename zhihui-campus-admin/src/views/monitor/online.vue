<template>
  <div class="online-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>在线用户</span>
          <el-button type="primary" @click="loadList">刷新</el-button>
        </div>
      </template>
      <el-table :data="onlineList" v-loading="loading" stripe>
        <el-table-column prop="tokenId" label="会话ID" width="120" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column prop="browser" label="浏览器" width="120" />
        <el-table-column prop="os" label="操作系统" width="120" />
        <el-table-column prop="loginTime" label="登录时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleForceLogout(row)">强退</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const onlineList = ref<any[]>([])

const loadList = async () => {
  loading.value = true
  try {
    // 模拟数据
    onlineList.value = [
      { tokenId: 'abc123', username: 'admin', realName: '管理员', ip: '192.168.1.100', browser: 'Chrome 120', os: 'Windows 10', loginTime: '2024-03-15 09:30:00' },
      { tokenId: 'def456', username: '2024001', realName: '张三', ip: '192.168.1.101', browser: 'Safari 17', os: 'macOS', loginTime: '2024-03-15 10:15:00' }
    ]
  } finally { loading.value = false }
}

const handleForceLogout = async (row: any) => {
  await ElMessageBox.confirm(`确认强制退出用户「${row.username}」？`, '提示')
  ElMessage.success('已强退')
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.online-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
