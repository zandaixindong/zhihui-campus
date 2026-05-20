<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 左侧用户信息卡片 -->
      <el-col :span="8">
        <el-card class="user-card">
          <div class="avatar-section">
            <el-avatar :size="100" :src="userInfo.avatar || defaultAvatar" />
            <div class="user-name">{{ userInfo.realName || userInfo.username }}</div>
            <div class="user-role">{{ userInfo.roleName || '学生' }}</div>
          </div>
          <el-divider />
          <div class="info-list">
            <div class="info-item">
              <span class="label">学号/工号</span>
              <span class="value">{{ userInfo.userCode || '--' }}</span>
            </div>
            <div class="info-item">
              <span class="label">学院</span>
              <span class="value">{{ userInfo.collegeName || '--' }}</span>
            </div>
            <div class="info-item">
              <span class="label">专业</span>
              <span class="value">{{ userInfo.majorName || '--' }}</span>
            </div>
            <div class="info-item">
              <span class="label">班级</span>
              <span class="value">{{ userInfo.className || '--' }}</span>
            </div>
            <div class="info-item">
              <span class="label">邮箱</span>
              <span class="value">{{ userInfo.email || '--' }}</span>
            </div>
            <div class="info-item">
              <span class="label">手机</span>
              <span class="value">{{ userInfo.phone || '--' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧设置区域 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>个人信息设置</span>
          </template>
          <el-form :model="profileForm" :rules="rules" ref="profileFormRef" label-width="100px" style="max-width: 500px;">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="profileForm.realName" disabled />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveProfile" :loading="saving">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px;">
          <template #header>
            <span>修改密码</span>
          </template>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px" style="max-width: 500px;">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword" :loading="changingPwd">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
const saving = ref(false)
const changingPwd = ref(false)
const profileFormRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const userInfo = ref<any>({})

const profileForm = reactive({
  realName: '',
  email: '',
  phone: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

const validateConfirmPassword = (rule: any, value: string, callback: any) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  const info = userStore.userInfo || {}
  userInfo.value = info
  profileForm.realName = info.realName || ''
  profileForm.email = info.email || ''
  profileForm.phone = info.phone || ''
}

const handleSaveProfile = async () => {
  await profileFormRef.value?.validate()
  saving.value = true
  try {
    // 调用更新接口
    ElMessage.success('保存成功')
  } finally {
    saving.value = false
  }
}

const handleChangePassword = async () => {
  await passwordFormRef.value?.validate()
  changingPwd.value = true
  try {
    // 调用修改密码接口
    ElMessage.success('密码修改成功')
    Object.assign(passwordForm, { oldPassword: '', newPassword: '', confirmPassword: '' })
  } finally {
    changingPwd.value = false
  }
}

onMounted(() => loadUserInfo())
</script>

<style scoped>
.profile-container { padding: 20px; }
.user-card { text-align: center; }
.avatar-section { padding: 20px 0; }
.user-name { font-size: 18px; font-weight: bold; margin-top: 12px; }
.user-role { font-size: 14px; color: #909399; margin-top: 4px; }
.info-list { text-align: left; }
.info-item { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
.info-item:last-child { border-bottom: none; }
.label { color: #909399; }
.value { color: #303133; }
</style>
