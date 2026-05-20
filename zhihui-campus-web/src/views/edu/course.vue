<template>
  <div class="course-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的课程</span>
          <el-select v-model="semesterId" placeholder="选择学期" clearable @change="loadCourses" style="width: 200px;">
            <el-option v-for="s in semesters" :key="s.id" :label="s.name" :value="s.id" />
          </el-select>
        </div>
      </template>
      <el-table :data="courseList" v-loading="loading" stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="courseCode" label="课程代码" width="120" />
        <el-table-column prop="teacherName" label="授课教师" width="120" />
        <el-table-column prop="credits" label="学分" width="80" align="center" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column prop="schedule" label="上课时间" width="150" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '进行中' : '已结束' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEvaluate(row)" v-if="row.canEvaluate">评价</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && courseList.length === 0" description="暂无课程" />
    </el-card>

    <!-- 教学评价对话框 -->
    <el-dialog v-model="evalVisible" title="教学评价" width="500px">
      <el-form :model="evalForm" label-width="100px">
        <el-form-item label="课程">{{ evalForm.courseName }}</el-form-item>
        <el-form-item label="评分">
          <el-rate v-model="evalForm.score" :max="5" show-score />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evalForm.content" type="textarea" :rows="4" placeholder="请输入您的评价" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="evalVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEval">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSelectedCourses, getSemesterList, submitEvaluation } from '@/api/edu'

const loading = ref(false)
const evalVisible = ref(false)
const courseList = ref<any[]>([])
const semesters = ref<any[]>([])
const semesterId = ref<number | undefined>(undefined)

const evalForm = reactive({
  courseOfferId: 0,
  courseName: '',
  score: 5,
  content: ''
})

const loadCourses = async () => {
  loading.value = true
  try {
    const res = await getSelectedCourses({ semesterId: semesterId.value })
    courseList.value = res.data || []
  } finally {
    loading.value = false
  }
}

const loadSemesters = async () => {
  try {
    const res = await getSemesterList()
    semesters.value = res.data || []
  } catch {}
}

const handleEvaluate = (row: any) => {
  evalForm.courseOfferId = row.id
  evalForm.courseName = row.courseName
  evalForm.score = 5
  evalForm.content = ''
  evalVisible.value = true
}

const handleSubmitEval = async () => {
  if (!evalForm.content) {
    ElMessage.warning('请输入评价内容')
    return
  }
  await submitEvaluation(evalForm)
  ElMessage.success('评价提交成功')
  evalVisible.value = false
  loadCourses()
}

onMounted(() => { loadCourses(); loadSemesters() })
</script>

<style scoped>
.course-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
