<template>
  <div class="server-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header><span>CPU信息</span></template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="核心数">{{ serverInfo.cpu?.cores || '--' }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-progress :percentage="serverInfo.cpu?.usage || 0" :color="getProgressColor(serverInfo.cpu?.usage || 0)" />
            </el-descriptions-item>
            <el-descriptions-item label="系统">{{ serverInfo.cpu?.sys || '--' }}</el-descriptions-item>
            <el-descriptions-item label="用户">{{ serverInfo.cpu?.user || '--' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>内存信息</span></template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="总内存">{{ serverInfo.memory?.total || '--' }}</el-descriptions-item>
            <el-descriptions-item label="已使用">{{ serverInfo.memory?.used || '--' }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-progress :percentage="serverInfo.memory?.usage || 0" :color="getProgressColor(serverInfo.memory?.usage || 0)" />
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header><span>JVM信息</span></template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="Java版本">{{ serverInfo.jvm?.version || '--' }}</el-descriptions-item>
            <el-descriptions-item label="最大内存">{{ serverInfo.jvm?.maxMemory || '--' }}</el-descriptions-item>
            <el-descriptions-item label="已分配">{{ serverInfo.jvm?.allocatedMemory || '--' }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-progress :percentage="serverInfo.jvm?.usage || 0" :color="getProgressColor(serverInfo.jvm?.usage || 0)" />
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>磁盘信息</span></template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="总空间">{{ serverInfo.disk?.total || '--' }}</el-descriptions-item>
            <el-descriptions-item label="已使用">{{ serverInfo.disk?.used || '--' }}</el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-progress :percentage="serverInfo.disk?.usage || 0" :color="getProgressColor(serverInfo.disk?.usage || 0)" />
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const serverInfo = ref<any>({
  cpu: { cores: 8, usage: 35, sys: '25%', user: '10%' },
  memory: { total: '16GB', used: '8.5GB', usage: 53 },
  jvm: { version: '17.0.2', maxMemory: '512MB', allocatedMemory: '256MB', usage: 45 },
  disk: { total: '500GB', used: '180GB', usage: 36 }
})

const getProgressColor = (percentage: number) => {
  if (percentage < 50) return '#67c23a'
  if (percentage < 80) return '#e6a23c'
  return '#f56c6c'
}
</script>

<style scoped>
.server-container { padding: 20px; }
</style>
