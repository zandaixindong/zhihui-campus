<template>
  <div class="notice-detail-container">
    <el-card v-loading="loading">
      <div class="detail-header">
        <el-button link @click="router.back()">← 返回列表</el-button>
      </div>
      <div class="detail-title">{{ notice.title }}</div>
      <div class="detail-meta">
        <span>发布单位：{{ notice.publisher }}</span>
        <span>发布时间：{{ notice.createTime }}</span>
        <span>阅读量：{{ notice.viewCount || 0 }}</span>
      </div>
      <el-divider />
      <div class="detail-content" v-html="notice.content"></div>
      <div class="detail-attachments" v-if="notice.attachments?.length">
        <div class="attachment-title">附件：</div>
        <div v-for="(file, index) in notice.attachments" :key="index" class="attachment-item">
          <el-link type="primary" :href="file.url">{{ file.name }}</el-link>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAnnouncementDetail } from '@/api/notice'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const notice = ref<any>({})

const loadDetail = async () => {
  const id = Number(route.params.id)
  if (!id) return
  loading.value = true
  try {
    const res = await getAnnouncementDetail(id)
    notice.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(() => loadDetail())
</script>

<style scoped>
.notice-detail-container { padding: 20px; max-width: 900px; margin: 0 auto; }
.detail-header { margin-bottom: 20px; }
.detail-title { font-size: 24px; font-weight: bold; color: #303133; text-align: center; margin-bottom: 16px; }
.detail-meta { text-align: center; font-size: 13px; color: #909399; display: flex; gap: 20px; justify-content: center; }
.detail-content { font-size: 15px; line-height: 1.8; color: #303133; padding: 20px 0; }
.detail-attachments { margin-top: 20px; padding-top: 20px; border-top: 1px solid #ebeef5; }
.attachment-title { font-weight: bold; margin-bottom: 10px; }
.attachment-item { margin-bottom: 5px; }
</style>
