<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-primary">
          <div class="stat-icon"><el-icon><User /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.userCount || 0 }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-success">
          <div class="stat-icon"><el-icon><View /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.todayVisits || 0 }}</div>
            <div class="stat-label">今日访问</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-warning">
          <div class="stat-icon"><el-icon><Document /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.noticeCount || 0 }}</div>
            <div class="stat-label">公告数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-danger">
          <div class="stat-icon"><el-icon><Connection /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.onlineCount || 0 }}</div>
            <div class="stat-label">在线用户</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 访问量趋势 -->
      <el-col :span="16">
        <el-card>
          <template #header><span>访问量趋势</span></template>
          <div ref="visitChart" style="height: 350px;"></div>
        </el-card>
      </el-col>

      <!-- 功能使用排行 -->
      <el-col :span="8">
        <el-card>
          <template #header><span>功能使用排行</span></template>
          <div ref="functionChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { User, View, Document, Connection } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getDashboardStats } from '@/api/statistics'

const stats = ref<any>({})
const visitChart = ref<HTMLElement | null>(null)
const functionChart = ref<HTMLElement | null>(null)
let visitChartInstance: echarts.ECharts | null = null
let functionChartInstance: echarts.ECharts | null = null

const loadStats = async () => {
  try {
    const res = await getDashboardStats()
    stats.value = res.data || {}
  } catch {
    // 模拟数据
    stats.value = {
      userCount: 12580,
      todayVisits: 3562,
      noticeCount: 156,
      onlineCount: 423
    }
  }
}

const initVisitChart = () => {
  if (!visitChart.value) return
  visitChartInstance = echarts.init(visitChart.value)
  visitChartInstance.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [{
      name: '访问量',
      type: 'line',
      smooth: true,
      data: [820, 932, 901, 1234, 1290, 1330, 1520],
      areaStyle: { opacity: 0.3 },
      itemStyle: { color: '#409eff' }
    }]
  })
}

const initFunctionChart = () => {
  if (!functionChart.value) return
  functionChartInstance = echarts.init(functionChart.value)
  functionChartInstance.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 35, name: '课表查询' },
        { value: 25, name: '成绩查询' },
        { value: 20, name: '一卡通' },
        { value: 15, name: '报修服务' },
        { value: 5, name: '其他' }
      ]
    }]
  })
}

const handleResize = () => {
  visitChartInstance?.resize()
  functionChartInstance?.resize()
}

onMounted(() => {
  loadStats()
  initVisitChart()
  initFunctionChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  visitChartInstance?.dispose()
  functionChartInstance?.dispose()
})
</script>

<style scoped>
.dashboard-container { padding: 20px; }
.stats-row { margin-bottom: 20px; }
.stat-card { display: flex; align-items: center; padding: 20px; }
.stat-card :deep(.el-card__body) { display: flex; align-items: center; width: 100%; }
.stat-icon { font-size: 48px; margin-right: 20px; }
.stat-primary .stat-icon { color: #409eff; }
.stat-success .stat-icon { color: #67c23a; }
.stat-warning .stat-icon { color: #e6a23c; }
.stat-danger .stat-icon { color: #f56c6c; }
.stat-value { font-size: 28px; font-weight: bold; color: #303133; }
.stat-label { font-size: 14px; color: #909399; margin-top: 4px; }
</style>
