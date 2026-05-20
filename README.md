# 智慧校园服务平台

一站式校园综合服务平台，打破校内数据孤岛，对接教务、学工等系统，为师生提供便捷的校园生活服务。

## 项目概览

| 模块 | 技术栈 | 说明 |
|------|--------|------|
| 后端服务 | Spring Boot 3 + Spring Cloud + MyBatis Plus | 微服务架构 |
| Web前端 | Vue 3 + TypeScript + Element Plus | 学生端 |
| 管理后台 | Vue 3 + Element Plus + ECharts | 管理端 |
| 移动端 | Flutter + Riverpod + Go Router | Android/iOS |
| 数据库 | MySQL 8.0 + Redis | 数据存储 |
| 部署 | Docker + Nacos + Gateway | 服务治理 |

## 项目结构

```
zhihui-campus/
├── zhihui-campus-server/          # 后端微服务
│   ├── zhihui-gateway/            # API网关 (8080)
│   ├── zhihui-auth/               # 认证服务 (8081)
│   ├── zhihui-system/             # 系统管理服务 (8082)
│   ├── zhihui-edu/                # 教务服务 (8083)
│   ├── zhihui-student/            # 学工服务 (8084)
│   ├── zhihui-life/               # 生活服务 (8085)
│   ├── zhihui-notice/             # 通知服务 (8086)
│   └── zhihui-common/             # 公共模块
│       ├── common-core/           # 核心工具 (R.java, BaseEntity, 异常处理)
│       ├── common-mybatis/        # MyBatis Plus配置
│       ├── common-redis/          # Redis配置与工具
│       ├── common-security/       # Spring Security + JWT
│       └── common-swagger/        # 接口文档 (Knife4j)
├── zhihui-campus-web/             # Web前端 (5173)
├── zhihui-campus-admin/           # 管理后台 (5174)
├── zhihui-campus-app/             # Flutter App
└── sql/                           # 数据库脚本
```

## 功能模块

### 系统管理 (zhihui-system)
- 用户管理：CRUD、导入导出、重置密码、多角色支持
- 角色管理：CRUD、权限分配（菜单级）
- 菜单管理：树形结构、目录/菜单/按钮三级
- 字典管理：字典类型与数据维护
- 学院专业：学院、专业、班级层级管理
- 日志管理：操作日志、登录日志

### 认证服务 (zhihui-auth)
- JWT Token 认证
- 统一登录/登出接口
- 用户信息获取
- 预留 CAS/OAuth2 对接

### 学工服务 (zhihui-student)
- 请假申请：事假/病假/其他，辅导员+学院两级审批
- 奖惩记录：奖励/惩罚查询
- 勤工助学：岗位浏览、在线申请、申请状态跟踪
- 心理咨询：预约咨询师、选择时间段、取消预约

### 生活服务 (zhihui-life)
- 一卡通：余额查询、消费记录、充值
- 报修服务：提交工单、进度跟踪、取消
- 场地预约：场地浏览、在线预约、我的预约管理
- 失物招领：发布寻物/招领信息、搜索

### 通知服务 (zhihui-notice)
- 公告管理：发布/编辑/删除、分类（学校/学院/教务/学工）
- 消息中心：站内信、未读统计、标记已读
- WebSocket 实时推送

### 教务服务 (zhihui-edu)
- 课表查询：周视图展示、按学期查询
- 成绩查询：分页列表、GPA统计（加权平均分、总学分）
- 考试安排：时间、地点、座位号
- 选课/退课：在线选课、容量检查、退课
- 学期管理：学期列表、当前学期
- 缓存策略：Redis缓存课表和GPA数据

### 校园社区
- 校园论坛：发帖、评论、点赞
- 搜索功能
- 精华帖标记

## Web前端页面

| 路径 | 页面 | 说明 |
|------|------|------|
| `/login` | 登录页 | 账号密码登录 |
| `/dashboard` | 首页 | 数据概览 |
| `/edu/schedule` | 我的课表 | 周视图课表 |
| `/edu/score` | 成绩查询 | GPA统计+成绩列表 |
| `/edu/exam` | 考试安排 | 考试时间地点 |
| `/edu/course` | 我的课程 | 课程列表+教学评价 |
| `/student/leave` | 请假申请 | 申请/查询/撤回 |
| `/student/reward` | 奖惩记录 | 奖励/惩罚查询 |
| `/student/work-study` | 勤工助学 | 岗位浏览/申请 |
| `/student/counseling` | 心理咨询 | 预约咨询 |
| `/life/card` | 一卡通 | 余额/消费/充值 |
| `/life/repair` | 报修服务 | 提交/跟踪工单 |
| `/life/booking` | 场地预约 | 浏览/预约场地 |
| `/life/lost-found` | 失物招领 | 发布/搜索 |
| `/notice/list` | 公告列表 | 公告浏览 |
| `/notice/detail/:id` | 公告详情 | 公告内容 |
| `/notice/message` | 消息中心 | 站内信 |
| `/community/post` | 校园论坛 | 帖子列表 |
| `/community/post/:id` | 帖子详情 | 评论互动 |
| `/profile` | 个人中心 | 信息修改/密码 |

## 管理后台页面

| 路径 | 页面 | 说明 |
|------|------|------|
| `/login` | 登录 | 管理员登录 |
| `/dashboard` | 数据看板 | 统计图表 |
| `/system/user` | 用户管理 | CRUD+重置密码 |
| `/system/role` | 角色管理 | 权限分配 |
| `/system/menu` | 菜单管理 | 树形结构 |
| `/system/dict` | 字典管理 | 字典类型/数据 |
| `/system/college` | 学院管理 | 学院/专业 |
| `/system/log` | 日志管理 | 操作/登录日志 |
| `/content/notice` | 公告管理 | 发布/编辑公告 |
| `/monitor/server` | 服务器监控 | CPU/内存/JVM |
| `/monitor/online` | 在线用户 | 在线列表/强退 |

## 后端API接口

### 认证接口
```
POST   /auth/login              # 登录
POST   /auth/logout             # 登出
GET    /auth/user-info           # 获取用户信息
```

### 系统管理接口
```
GET    /system/user/list         # 用户列表
POST   /system/user              # 新增用户
PUT    /system/user              # 更新用户
DELETE /system/user/{id}         # 删除用户
PUT    /system/user/reset-password # 重置密码
GET    /system/role/list         # 角色列表
POST   /system/role              # 新增角色
PUT    /system/role              # 更新角色
DELETE /system/role/{id}         # 删除角色
PUT    /system/role/permissions  # 分配权限
GET    /system/menu/list         # 菜单列表
POST   /system/menu              # 新增菜单
PUT    /system/menu              # 更新菜单
DELETE /system/menu/{id}         # 删除菜单
GET    /system/dict/type/list    # 字典类型列表
GET    /system/dict/data/list    # 字典数据列表
GET    /system/college/list      # 学院列表
GET    /system/major/list        # 专业列表
GET    /system/log/oper/list     # 操作日志
GET    /system/log/login/list    # 登录日志
```

### 学工服务接口
```
GET    /student/leave/list       # 请假列表
POST   /student/leave            # 提交请假
PUT    /student/leave/counselor/approve/{id}  # 辅导员审批
PUT    /student/leave/college/approve/{id}    # 学院审批
DELETE /student/leave/{id}       # 取消请假
GET    /student/reward-punishment/list  # 奖惩记录
GET    /student/work-study/positions    # 岗位列表
POST   /student/work-study/applications # 提交申请
DELETE /student/work-study/applications/{id} # 取消申请
GET    /student/counseling/appointments # 预约列表
POST   /student/counseling/appointments # 提交预约
PUT    /student/counseling/appointments/cancel/{id} # 取消预约
```

### 生活服务接口
```
GET    /life/card/balance        # 一卡通余额
GET    /life/card/transactions   # 消费记录
POST   /life/card/recharge       # 充值
GET    /life/repair/list         # 报修列表
POST   /life/repair              # 提交报修
PUT    /life/repair/cancel/{id}  # 取消报修
GET    /life/venue/list          # 场地列表
POST   /life/venue/booking       # 提交预约
PUT    /life/venue/booking/cancel/{id} # 取消预约
GET    /life/lost-found/list     # 失物招领列表
POST   /life/lost-found          # 发布信息
```

### 通知服务接口
```
GET    /notice/announcement/list      # 公告列表
GET    /notice/announcement/{id}      # 公告详情
POST   /notice/announcement           # 发布公告
PUT    /notice/announcement           # 编辑公告
DELETE /notice/announcement/{id}      # 删除公告
GET    /notice/message/list           # 消息列表
GET    /notice/message/unread/count   # 未读数量
POST   /notice/message                # 发送消息
PUT    /notice/message/read/{id}      # 标记已读
PUT    /notice/message/read/all       # 全部已读
```

### 教务接口（待对接正方系统）
```
GET    /edu/schedule/current     # 当前课表
GET    /edu/schedule/week/{w}    # 指定周课表
GET    /edu/score/list           # 成绩列表
GET    /edu/score/gpa            # GPA统计
GET    /edu/exam/list            # 考试安排
GET    /edu/course/selected      # 已选课程
POST   /edu/evaluation/submit    # 提交评价
GET    /edu/semester/list        # 学期列表
```

## 数据库设计

核心数据表：

| 分类 | 表名 | 说明 |
|------|------|------|
| 系统 | sys_user | 用户表 |
| 系统 | sys_role | 角色表 |
| 系统 | sys_menu | 菜单表 |
| 系统 | sys_user_role | 用户角色关联 |
| 系统 | sys_role_menu | 角色菜单关联 |
| 系统 | sys_dict_type | 字典类型 |
| 系统 | sys_dict_data | 字典数据 |
| 系统 | sys_oper_log | 操作日志 |
| 系统 | sys_login_log | 登录日志 |
| 组织 | college | 学院 |
| 组织 | major | 专业 |
| 学工 | student_leave_application | 请假申请 |
| 学工 | student_reward_punishment | 奖惩记录 |
| 学工 | student_work_study | 勤工助学岗位 |
| 学工 | student_work_study_application | 岗位申请 |
| 学工 | student_counseling_appointment | 心理咨询预约 |
| 生活 | life_card_transaction | 一卡通交易 |
| 生活 | life_repair_order | 报修工单 |
| 生活 | life_venue | 场地 |
| 生活 | life_venue_booking | 场地预约 |
| 生活 | life_lost_found | 失物招领 |
| 通知 | notice_announcement | 公告 |
| 通知 | notice_message | 消息 |
| 教务 | edu_course | 课程 |
| 教务 | edu_score | 成绩 |
| 教务 | edu_exam | 考试安排 |
| 教务 | edu_semester | 学期 |

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 6+
- Nacos 2.x
- Flutter 3.x (移动端)

### 后端启动
```bash
cd zhihui-campus-server

# 1. 创建数据库并执行初始化脚本
mysql -u root -p < sql/zhihui_campus.sql

# 2. 启动Nacos
sh nacos/bin/startup.sh -m standalone

# 3. 编译项目
mvn clean install -DskipTests

# 4. 按顺序启动服务
cd zhihui-gateway && mvn spring-boot:run
cd zhihui-auth && mvn spring-boot:run
cd zhihui-system && mvn spring-boot:run
cd zhihui-student && mvn spring-boot:run
cd zhihui-life && mvn spring-boot:run
cd zhihui-notice && mvn spring-boot:run
```

### Web前端启动
```bash
cd zhihui-campus-web
npm install
npm run dev
# 访问 http://localhost:5173
```

### 管理后台启动
```bash
cd zhihui-campus-admin
npm install
npm run dev
# 访问 http://localhost:5174
```

### Flutter App启动
```bash
cd zhihui-campus-app
flutter pub get
flutter run
```

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 测试学生 | 2024001 | 123456 |

## 开发进度

- [x] 项目架构搭建
- [x] 公共模块（common-core/mybatis/redis/security/swagger）
- [x] API网关
- [x] 认证服务
- [x] 系统管理模块
- [x] 学工服务模块
- [x] 生活服务模块
- [x] 通知服务模块
- [x] 教务服务模块（课表/成绩/考试/选课/学期）
- [x] Web前端完整页面
- [x] 管理后台完整页面
- [x] Flutter App基础框架
- [ ] 正方教务系统对接（CAS认证、数据同步）
- [ ] 社区功能完善
- [ ] AI智能问答
- [ ] Docker部署配置
- [ ] CI/CD流水线

## 待办事项

### 优先级高
- 对接正方教务系统（CAS认证、数据同步）
- 完善社区功能（发帖、评论接口）
- 添加数据校验和权限控制

### 优先级中
- Docker Compose 部署配置
- 接口文档完善（Knife4j）
- 前端国际化支持
- App推送服务集成

### 优先级低
- AI智能问答（大模型对接）
- 数据分析看板
- 微信小程序版本

## 技术要点

### 认证流程
1. 用户提交账号密码到 `/auth/login`
2. 后端验证密码，生成 JWT Token 返回
3. 前端存储 Token 到 Cookie
4. 后续请求携带 `Authorization: Bearer {token}`
5. 网关统一校验 Token，转发到对应服务

### 微服务通信
- 服务注册发现：Nacos
- 服务间调用：OpenFeign
- 统一网关：Spring Cloud Gateway
- 配置中心：Nacos Config

### 前端状态管理
- 用户状态：Pinia store
- Token管理：Cookie
- 路由守卫：Vue Router beforeEach
- 请求拦截：Axios interceptors

## 贡献指南

1. 从 `master` 创建功能分支：`git checkout -b feature/xxx`
2. 提交修改：`git commit -m "feat: 添加xxx功能"`
3. 推送分支：`git push origin feature/xxx`
4. 创建 Pull Request 到 `master`
5. 代码审查后合并

### Commit 规范
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具相关

## 许可证

MIT License
