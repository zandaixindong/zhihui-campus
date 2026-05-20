<template>
  <div class="error-page">
    <div class="error-content">
      <h1 class="error-code">404</h1>
      <p class="error-message">抱歉，您访问的页面不存在</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
const goHome = () => { router.push('/') }
</script>

<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f5f7fa;
}
.error-content { text-align: center; }
.error-code { font-size: 120px; font-weight: bold; color: #409eff; margin-bottom: 20px; }
.error-message { font-size: 18px; color: #606266; margin-bottom: 30px; }
</style>
