<template>
  <div class="card-container">
    <!-- 一卡通余额卡片 -->
    <el-row :gutter="20" class="balance-row">
      <el-col :span="8">
        <el-card class="balance-card" shadow="hover">
          <div class="balance-info">
            <div class="balance-label">卡余额</div>
            <div class="balance-amount">¥{{ cardInfo.balance?.toFixed(2) || '0.00' }}</div>
            <div class="card-no">卡号：{{ cardInfo.cardNo || '--' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-info">
            <div class="stat-label">本月消费</div>
            <div class="stat-amount">¥{{ monthlyExpense?.toFixed(2) || '0.00' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-info">
            <div class="stat-label">本月充值</div>
            <div class="stat-amount">¥{{ monthlyRecharge?.toFixed(2) || '0.00' }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 消费记录 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>消费记录</span>
          <el-radio-group v-model="queryType" size="small" @change="handleTypeChange">
            <el-radio-button :value="0">全部</el-radio-button>
            <el-radio-button :value="1">消费</el-radio-button>
            <el-radio-button :value="2">充值</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <el-table :data="transactionList" v-loading="loading" stripe>
        <el-table-column prop="transactionTime" label="时间" width="170" />
        <el-table-column prop="type" label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'danger' : 'success'">{{ row.type === 1 ? '消费' : '充值' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="{ row }">
            <span :class="row.type === 1 ? 'expense' : 'income'">
              {{ row.type === 1 ? '-' : '+' }}¥{{ row.amount?.toFixed(2) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="120" align="right">
          <template #default="{ row }">¥{{ row.balance?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="location" label="地点" />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      </el-table>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadTransactions"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCardBalance, getCardTransactions } from '@/api/life'

const loading = ref(false)
const cardInfo = ref<any>({})
const transactionList = ref<any[]>([])
const monthlyExpense = ref(0)
const monthlyRecharge = ref(0)
const queryType = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadCardInfo = async () => {
  try {
    const res = await getCardBalance(0) // userId从token获取
    cardInfo.value = res.data
    monthlyExpense.value = res.data.monthlyExpense || 0
    monthlyRecharge.value = res.data.monthlyRecharge || 0
  } catch {}
}

const loadTransactions = async () => {
  loading.value = true
  try {
    const params: any = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (queryType.value) params.type = queryType.value
    const res = await getCardTransactions(params)
    transactionList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleTypeChange = () => { pageNum.value = 1; loadTransactions() }

onMounted(() => { loadCardInfo(); loadTransactions() })
</script>

<style scoped>
.card-container { padding: 20px; }
.balance-row { margin-bottom: 20px; }
.balance-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
.balance-info { text-align: center; padding: 20px 0; }
.balance-label { font-size: 14px; opacity: 0.8; }
.balance-amount { font-size: 36px; font-weight: bold; margin: 10px 0; }
.card-no { font-size: 12px; opacity: 0.7; }
.stat-card { text-align: center; padding: 20px 0; }
.stat-label { font-size: 14px; color: #909399; }
.stat-amount { font-size: 24px; font-weight: bold; margin-top: 10px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.expense { color: #f56c6c; }
.income { color: #67c23a; }
</style>
