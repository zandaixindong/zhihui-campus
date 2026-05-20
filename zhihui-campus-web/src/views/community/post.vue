<template>
  <div class="post-container">
    <el-card class="search-card">
      <el-form inline>
        <el-form-item>
          <el-input v-model="keyword" placeholder="搜索帖子" clearable @keyup.enter="handleSearch" style="width: 300px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="success" @click="handlePublish">发帖</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="最新" name="latest" />
        <el-tab-pane label="热门" name="hot" />
        <el-tab-pane label="精华" name="elite" />
      </el-tabs>

      <div class="post-list">
        <div v-for="post in postList" :key="post.id" class="post-item" @click="handleDetail(post)">
          <div class="post-header">
            <el-avatar :size="40" :src="post.authorAvatar" />
            <div class="post-meta">
              <div class="author-name">{{ post.authorName }}</div>
              <div class="post-time">{{ post.createTime }}</div>
            </div>
            <el-tag v-if="post.isElite" type="warning" size="small">精华</el-tag>
          </div>
          <div class="post-title">{{ post.title }}</div>
          <div class="post-summary">{{ post.content?.substring(0, 150) }}...</div>
          <div class="post-footer">
            <span><el-icon><View /></el-icon> {{ post.viewCount || 0 }}</span>
            <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount || 0 }}</span>
            <span><el-icon><Star /></el-icon> {{ post.likeCount || 0 }}</span>
          </div>
        </div>
        <el-empty v-if="!loading && postList.length === 0" description="暂无帖子" />
      </div>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @change="loadPosts"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { View, ChatDotRound, Star } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const keyword = ref('')
const activeTab = ref('latest')
const postList = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 模拟数据，实际应调用API
const loadPosts = async () => {
  loading.value = true
  try {
    // const res = await getPostList({ sort: activeTab.value, keyword: keyword.value, pageNum: pageNum.value, pageSize: pageSize.value })
    // postList.value = res.data.records
    // total.value = res.data.total
    postList.value = [
      { id: 1, title: '有没有一起组队参加编程比赛的同学？', content: '学校马上要举办编程比赛了，想找个队友一起...', authorName: '张三', createTime: '2024-03-15 10:30', viewCount: 128, commentCount: 15, likeCount: 32 },
      { id: 2, title: '图书馆自习位置推荐', content: '分享一下图书馆比较安静的自习位置...', authorName: '李四', createTime: '2024-03-14 16:20', viewCount: 256, commentCount: 28, likeCount: 45 },
      { id: 3, title: '食堂美食推荐', content: '东区食堂二楼的麻辣香锅真的太好吃了...', authorName: '王五', createTime: '2024-03-14 12:00', viewCount: 380, commentCount: 42, likeCount: 68 }
    ]
    total.value = 3
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { pageNum.value = 1; loadPosts() }
const handleTabChange = () => { pageNum.value = 1; loadPosts() }
const handleDetail = (post: any) => { router.push(`/community/post/${post.id}`) }
const handlePublish = () => { /* 打开发帖对话框 */ }

onMounted(() => loadPosts())
</script>

<style scoped>
.post-container { padding: 20px; }
.search-card { margin-bottom: 16px; }
.post-item { padding: 16px; border-bottom: 1px solid #ebeef5; cursor: pointer; transition: background 0.2s; }
.post-item:hover { background: #f5f7fa; }
.post-item:last-child { border-bottom: none; }
.post-header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.post-meta { flex: 1; }
.author-name { font-weight: bold; font-size: 14px; }
.post-time { font-size: 12px; color: #909399; }
.post-title { font-size: 16px; font-weight: bold; color: #303133; margin-bottom: 8px; }
.post-summary { font-size: 14px; color: #606266; line-height: 1.6; margin-bottom: 10px; }
.post-footer { display: flex; gap: 20px; font-size: 13px; color: #909399; }
.post-footer span { display: flex; align-items: center; gap: 4px; }
</style>
