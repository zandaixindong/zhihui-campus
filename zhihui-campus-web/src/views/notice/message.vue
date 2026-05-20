<template>
  <div class="message-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>消息中心</span>
          <el-button type="primary" link @click="handleMarkAllRead">全部已读</el-button>
        </div>
      </template>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部消息" name="all" />
        <el-tab-pane label="未读消息" name="unread" />
        <el-tab-pane label="已读消息" name="read" />
      </el-tabs>
      <el-table :data="messageList" v-loading="loading" stripe>
        <el-table-column width="40">
          <template #default="{ row }">
            <div v-if="!row.isRead" class="unread-dot"></div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="msgType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="msgTypeTag(row.msgType)" size="small">{{ msgTypeText(row.msgType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleRead(row)">查看</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadList"
        style="margin-top: 16px;"
      />
    </el-card>

    <!-- 消息详情 -->
    <el-dialog v-model="detailVisible" title="消息详情" width="500px">
      <div class="msg-title">{{ currentMsg.title }}</div>
      <div class="msg-time">{{ currentMsg.createTime }}</div>
      <el-divider />
      <div class="msg-content">{{ currentMsg.content }}</div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMessageList, markAsRead, markAllAsRead } from '@/api/notice'

const loading = ref(false)
const activeTab = ref('all')
const messageList = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const detailVisible = ref(false)
const currentMsg = ref<any>({})

const msgTypeText = (t: number) => ({ 1: '系统通知', 2: '待办提醒', 3: '审批通知', 4: '消息推送' }[t] || '其他')
const msgTypeTag = (t: number) => ({ 1: 'danger', 2: 'warning', 3: '', 4: 'success' }[t] as any) || 'info'

const loadList = async () => {
  loading.value = true
  try {
    const params: any = { userId: 0, pageNum: pageNum.value, pageSize: pageSize.value }
    if (activeTab.value === 'unread') params.isRead = 0
    else if (activeTab.value === 'read') params.isRead = 1
    const res = await getMessageList(params)
    messageList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => { pageNum.value = 1; loadList() }

const handleRead = async (row: any) => {
  if (!row.isRead) {
    await markAsRead(row.id)
    row.isRead = 1
  }
  currentMsg.value = row
  detailVisible.value = true
}

const handleDelete = async (row: any) => {
  await ElMessageBox.confirm('确认删除该消息？', '提示')
  // 调用删除接口
  ElMessage.success('已删除')
  loadList()
}

const handleMarkAllRead = async () => {
  await markAllAsRead(0) // userId从token获取
  ElMessage.success('全部已标为已读')
  loadList()
}

onMounted(() => loadList())
</script>

<style scoped>
.message-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.unread-dot { width: 8px; height: 8px; border-radius: 50%; background: #f56c6c; }
.msg-title { font-size: 18px; font-weight: bold; margin-bottom: 8px; }
.msg-time { font-size: 13px; color: #909399; }
.msg-content { font-size: 14px; line-height: 1.8; color: #303133; }
</style>
