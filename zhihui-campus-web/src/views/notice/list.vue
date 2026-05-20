<template>
  <div class="notice-container">
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
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
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div class="notice-list">
        <div v-for="item in noticeList" :key="item.id" class="notice-item" @click="handleDetail(item)">
          <div class="notice-header">
            <el-tag :type="noticeTypeTag(item.noticeType)" size="small">{{ noticeTypeText(item.noticeType) }}</el-tag>
            <span class="notice-time">{{ item.createTime }}</span>
          </div>
          <div class="notice-title">{{ item.title }}</div>
          <div class="notice-summary">{{ item.content?.substring(0, 120) }}...</div>
          <div class="notice-footer">
            <span>发布单位：{{ item.publisher }}</span>
            <span>阅读量：{{ item.viewCount || 0 }}</span>
          </div>
        </div>
        <el-empty v-if="!loading && noticeList.length === 0" description="暂无公告" />
      </div>
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadList"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAnnouncementList } from '@/api/notice'

const router = useRouter()
const loading = ref(false)
const noticeList = ref<any[]>([])
const total = ref(0)

const queryParams = reactive({
  noticeType: undefined as number | undefined,
  pageNum: 1,
  pageSize: 10
})

const noticeTypeText = (t: number) => ({ 1: '学校公告', 2: '学院通知', 3: '教务通知', 4: '学工通知' }[t] || '其他')
const noticeTypeTag = (t: number) => ({ 1: 'danger', 2: 'warning', 3: '', 4: 'success' }[t] as any) || 'info'

const loadList = async () => {
  loading.value = true
  try {
    const res = await getAnnouncementList(queryParams)
    noticeList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNum = 1; loadList() }
const handleDetail = (item: any) => { router.push(`/notice/detail/${item.id}`) }

onMounted(() => loadList())
</script>

<style scoped>
.notice-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
.notice-item { padding: 16px; border-bottom: 1px solid #ebeef5; cursor: pointer; transition: background 0.2s; }
.notice-item:hover { background: #f5f7fa; }
.notice-item:last-child { border-bottom: none; }
.notice-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.notice-time { font-size: 12px; color: #909399; }
.notice-title { font-size: 16px; font-weight: bold; color: #303133; margin-bottom: 8px; }
.notice-summary { font-size: 14px; color: #606266; line-height: 1.6; margin-bottom: 10px; }
.notice-footer { font-size: 12px; color: #909399; display: flex; gap: 20px; }
</style>
