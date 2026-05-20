<template>
  <div class="post-detail-container">
    <el-card v-loading="loading">
      <div class="detail-header">
        <el-button link @click="router.back()">← 返回列表</el-button>
      </div>

      <div class="post-header">
        <el-avatar :size="50" :src="post.authorAvatar" />
        <div class="post-meta">
          <div class="author-name">{{ post.authorName }}</div>
          <div class="post-time">{{ post.createTime }}</div>
        </div>
      </div>

      <h1 class="post-title">{{ post.title }}</h1>
      <div class="post-content">{{ post.content }}</div>

      <div class="post-actions">
        <el-button :type="isLiked ? 'primary' : ''" @click="handleLike">
          <el-icon><Star /></el-icon> {{ post.likeCount || 0 }}
        </el-button>
        <el-button @click="scrollToComments">
          <el-icon><ChatDotRound /></el-icon> {{ post.commentCount || 0 }}
        </el-button>
      </div>
    </el-card>

    <!-- 评论区 -->
    <el-card class="comment-card" ref="commentCard">
      <template #header>
        <span>评论 ({{ comments.length }})</span>
      </template>

      <div class="comment-input">
        <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="说点什么..." />
        <el-button type="primary" @click="handleComment" :loading="commenting" style="margin-top: 10px;">发表评论</el-button>
      </div>

      <el-divider />

      <div class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <el-avatar :size="36" :src="comment.authorAvatar" />
          <div class="comment-body">
            <div class="comment-header">
              <span class="comment-author">{{ comment.authorName }}</span>
              <span class="comment-time">{{ comment.createTime }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
        </div>
        <el-empty v-if="comments.length === 0" description="暂无评论" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Star, ChatDotRound } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const commenting = ref(false)
const isLiked = ref(false)
const commentContent = ref('')
const commentCard = ref<any>(null)

const post = ref<any>({})
const comments = ref<any[]>([])

const loadPost = async () => {
  loading.value = true
  try {
    // 模拟数据
    post.value = {
      id: route.params.id,
      title: '有没有一起组队参加编程比赛的同学？',
      content: '学校马上要举办编程比赛了，想找个队友一起参加。比赛时间是下个月，需要2-3人组队。有兴趣的同学可以回复或者私信我。\n\n要求：\n1. 熟悉Java/Python/Go任一语言\n2. 有算法基础\n3. 能保证每周至少10小时的训练时间',
      authorName: '张三',
      authorAvatar: '',
      createTime: '2024-03-15 10:30',
      likeCount: 32,
      commentCount: 15
    }
    comments.value = [
      { id: 1, authorName: '李四', content: '我Java还行，算我一个！', createTime: '2024-03-15 11:20' },
      { id: 2, authorName: '王五', content: 'Python选手路过，可以一起吗？', createTime: '2024-03-15 12:05' },
      { id: 3, authorName: '赵六', content: '有算法竞赛经验，已私信', createTime: '2024-03-15 14:30' }
    ]
  } finally {
    loading.value = false
  }
}

const handleLike = () => {
  isLiked.value = !isLiked.value
  post.value.likeCount += isLiked.value ? 1 : -1
}

const handleComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  commenting.value = true
  try {
    // await submitComment({ postId: post.value.id, content: commentContent.value })
    comments.value.unshift({
      id: Date.now(),
      authorName: '我',
      content: commentContent.value,
      createTime: new Date().toLocaleString()
    })
    commentContent.value = ''
    ElMessage.success('评论成功')
  } finally {
    commenting.value = false
  }
}

const scrollToComments = () => {
  commentCard.value?.$el?.scrollIntoView({ behavior: 'smooth' })
}

onMounted(() => loadPost())
</script>

<style scoped>
.post-detail-container { padding: 20px; max-width: 900px; margin: 0 auto; }
.detail-header { margin-bottom: 20px; }
.post-header { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.author-name { font-weight: bold; font-size: 15px; }
.post-time { font-size: 13px; color: #909399; }
.post-title { font-size: 22px; font-weight: bold; color: #303133; margin-bottom: 16px; }
.post-content { font-size: 15px; line-height: 1.8; color: #303133; white-space: pre-wrap; }
.post-actions { margin-top: 20px; display: flex; gap: 10px; }
.comment-card { margin-top: 20px; }
.comment-input { margin-bottom: 10px; }
.comment-item { display: flex; gap: 12px; padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.comment-item:last-child { border-bottom: none; }
.comment-body { flex: 1; }
.comment-header { display: flex; justify-content: space-between; margin-bottom: 6px; }
.comment-author { font-weight: bold; font-size: 14px; }
.comment-time { font-size: 12px; color: #909399; }
.comment-content { font-size: 14px; color: #303133; line-height: 1.6; }
</style>
