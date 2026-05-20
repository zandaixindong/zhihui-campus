<template>
  <div class="reward-container">
    <el-card class="search-card">
      <el-form :model="queryParams" inline>
        <el-form-item label="记录类型">
          <el-select v-model="queryParams.type" placeholder="全部" clearable>
            <el-option label="奖励" :value="1" />
            <el-option label="惩罚" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="recordList" v-loading="loading" stripe>
        <el-table-column prop="type" label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'success' : 'danger'">{{ row.type === 1 ? '奖励' : '惩罚' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="level" label="级别" width="120" />
        <el-table-column prop="grantUnit" label="授予单位" width="150" />
        <el-table-column prop="grantDate" label="日期" width="120" />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadList"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getRewardPunishmentList } from '@/api/student'

const loading = ref(false)
const recordList = ref<any[]>([])
const total = ref(0)
const queryParams = reactive({ type: undefined as number | undefined, pageNum: 1, pageSize: 10 })

const loadList = async () => {
  loading.value = true
  try {
    const res = await getRewardPunishmentList(queryParams)
    recordList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNum = 1; loadList() }
onMounted(() => loadList())
</script>

<style scoped>
.reward-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
</style>
