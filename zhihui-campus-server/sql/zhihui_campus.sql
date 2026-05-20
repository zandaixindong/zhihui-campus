-- =============================================
-- 智慧校园服务平台 - 数据库初始化脚本
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `zhihui_campus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `zhihui_campus`;

-- =============================================
-- 1. 系统管理相关表
-- =============================================

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名（学号/工号）',
    `password` VARCHAR(200) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `user_type` TINYINT DEFAULT 1 COMMENT '用户类型（1学生2教师3管理员4校友）',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    `major_id` BIGINT DEFAULT NULL COMMENT '专业ID',
    `class_name` VARCHAR(50) DEFAULT NULL COMMENT '班级',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `sex` TINYINT DEFAULT 0 COMMENT '性别（0未知1男2女）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志（0存在1删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_college_id` (`college_id`),
    KEY `idx_user_type` (`user_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL COMMENT '角色ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_key` VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单权限表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL COMMENT '菜单ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `path` VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    `menu_type` CHAR(1) DEFAULT 'M' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `perms` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `icon` VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    `visible` TINYINT DEFAULT 1 COMMENT '是否可见（0隐藏1显示）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 字典类型表
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
    `id` BIGINT NOT NULL COMMENT '字典主键',
    `dict_name` VARCHAR(100) DEFAULT '' COMMENT '字典名称',
    `dict_type` VARCHAR(100) DEFAULT '' UNIQUE COMMENT '字典类型',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 字典数据表
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
    `id` BIGINT NOT NULL COMMENT '字典编码',
    `dict_sort` INT DEFAULT 0 COMMENT '字典排序',
    `dict_label` VARCHAR(100) DEFAULT '' COMMENT '字典标签',
    `dict_value` VARCHAR(100) DEFAULT '' COMMENT '字典键值',
    `dict_type` VARCHAR(100) DEFAULT '' COMMENT '字典类型',
    `css_class` VARCHAR(100) DEFAULT NULL COMMENT '样式属性',
    `list_class` VARCHAR(100) DEFAULT NULL COMMENT '表格回显样式',
    `is_default` CHAR(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 系统配置表
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` BIGINT NOT NULL COMMENT '参数主键',
    `config_name` VARCHAR(100) DEFAULT '' COMMENT '参数名称',
    `config_key` VARCHAR(100) DEFAULT '' COMMENT '参数键名',
    `config_value` VARCHAR(500) DEFAULT '' COMMENT '参数键值',
    `config_type` CHAR(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 操作日志表
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
    `id` BIGINT NOT NULL COMMENT '日志主键',
    `title` VARCHAR(50) DEFAULT '' COMMENT '模块标题',
    `method` VARCHAR(200) DEFAULT '' COMMENT '方法名称',
    `request_method` VARCHAR(10) DEFAULT '' COMMENT '请求方式',
    `oper_name` VARCHAR(50) DEFAULT '' COMMENT '操作人员',
    `oper_url` VARCHAR(255) DEFAULT '' COMMENT '请求URL',
    `oper_ip` VARCHAR(128) DEFAULT '' COMMENT '主机地址',
    `oper_location` VARCHAR(255) DEFAULT '' COMMENT '操作地点',
    `oper_param` VARCHAR(2000) DEFAULT '' COMMENT '请求参数',
    `json_result` VARCHAR(2000) DEFAULT '' COMMENT '返回参数',
    `status` TINYINT DEFAULT 1 COMMENT '操作状态（0异常1正常）',
    `error_msg` VARCHAR(2000) DEFAULT '' COMMENT '错误消息',
    `oper_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `cost_time` BIGINT DEFAULT 0 COMMENT '消耗时间',
    PRIMARY KEY (`id`),
    KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 登录日志表
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
    `id` BIGINT NOT NULL COMMENT '访问ID',
    `user_name` VARCHAR(50) DEFAULT '' COMMENT '用户账号',
    `ipaddr` VARCHAR(128) DEFAULT '' COMMENT '登录IP地址',
    `login_location` VARCHAR(255) DEFAULT '' COMMENT '登录地点',
    `browser` VARCHAR(50) DEFAULT '' COMMENT '浏览器类型',
    `os` VARCHAR(50) DEFAULT '' COMMENT '操作系统',
    `status` TINYINT DEFAULT 1 COMMENT '登录状态（0失败1成功）',
    `msg` VARCHAR(255) DEFAULT '' COMMENT '提示消息',
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (`id`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- =============================================
-- 2. 组织架构相关表
-- =============================================

-- 学院表
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college` (
    `id` BIGINT NOT NULL COMMENT '学院ID',
    `college_code` VARCHAR(20) NOT NULL COMMENT '学院代码',
    `college_name` VARCHAR(100) NOT NULL COMMENT '学院名称',
    `college_en_name` VARCHAR(200) DEFAULT NULL COMMENT '学院英文名称',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_college_code` (`college_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院表';

-- 专业表
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
    `id` BIGINT NOT NULL COMMENT '专业ID',
    `college_id` BIGINT NOT NULL COMMENT '所属学院ID',
    `major_code` VARCHAR(20) NOT NULL COMMENT '专业代码',
    `major_name` VARCHAR(100) NOT NULL COMMENT '专业名称',
    `major_en_name` VARCHAR(200) DEFAULT NULL COMMENT '专业英文名称',
    `degree_type` VARCHAR(20) DEFAULT NULL COMMENT '学位类型',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0禁用1正常）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_major_code` (`major_code`),
    KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- 班级表
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info` (
    `id` BIGINT NOT NULL COMMENT '班级ID',
    `major_id` BIGINT NOT NULL COMMENT '专业ID',
    `class_code` VARCHAR(20) NOT NULL COMMENT '班级代码',
    `class_name` VARCHAR(100) NOT NULL COMMENT '班级名称',
    `grade` VARCHAR(10) DEFAULT NULL COMMENT '年级',
    `admission_year` INT DEFAULT NULL COMMENT '入学年份',
    `counselor_id` BIGINT DEFAULT NULL COMMENT '辅导员ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_class_code` (`class_code`),
    KEY `idx_major_id` (`major_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- =============================================
-- 3. 教务相关表（从正方同步）
-- =============================================

-- 学期表
DROP TABLE IF EXISTS `edu_semester`;
CREATE TABLE `edu_semester` (
    `id` BIGINT NOT NULL COMMENT '学期ID',
    `semester_code` VARCHAR(20) NOT NULL COMMENT '学期代码（如2024-2025-1）',
    `semester_name` VARCHAR(50) NOT NULL COMMENT '学期名称',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `weeks` INT DEFAULT 20 COMMENT '总周数',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_semester_code` (`semester_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学期表';

-- 课程表
DROP TABLE IF EXISTS `edu_course`;
CREATE TABLE `edu_course` (
    `id` BIGINT NOT NULL COMMENT '课程ID',
    `course_code` VARCHAR(30) NOT NULL COMMENT '课程代码',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `course_en_name` VARCHAR(200) DEFAULT NULL COMMENT '课程英文名称',
    `credits` DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    `hours` INT DEFAULT NULL COMMENT '学时',
    `course_type` VARCHAR(20) DEFAULT NULL COMMENT '课程类型',
    `college_id` BIGINT DEFAULT NULL COMMENT '开课学院ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_code` (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 开课表（课程实例）
DROP TABLE IF EXISTS `edu_course_offer`;
CREATE TABLE `edu_course_offer` (
    `id` BIGINT NOT NULL COMMENT '开课ID',
    `semester_id` BIGINT NOT NULL COMMENT '学期ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `teacher_id` BIGINT DEFAULT NULL COMMENT '教师ID',
    `teacher_name` VARCHAR(50) DEFAULT NULL COMMENT '教师姓名',
    `capacity` INT DEFAULT NULL COMMENT '容量',
    `selected` INT DEFAULT 0 COMMENT '已选人数',
    `classroom` VARCHAR(50) DEFAULT NULL COMMENT '教室',
    `schedule_info` VARCHAR(500) DEFAULT NULL COMMENT '上课时间信息',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_semester_course` (`semester_id`, `course_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开课表';

-- 学生选课表
DROP TABLE IF EXISTS `edu_student_course`;
CREATE TABLE `edu_student_course` (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `course_offer_id` BIGINT NOT NULL COMMENT '开课ID',
    `select_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态（1已选2已退）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_offer` (`student_id`, `course_offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课表';

-- 成绩表
DROP TABLE IF EXISTS `edu_score`;
CREATE TABLE `edu_score` (
    `id` BIGINT NOT NULL COMMENT '成绩ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `course_offer_id` BIGINT DEFAULT NULL COMMENT '开课ID',
    `semester_id` BIGINT DEFAULT NULL COMMENT '学期ID',
    `score` DECIMAL(5,2) DEFAULT NULL COMMENT '成绩',
    `score_level` VARCHAR(10) DEFAULT NULL COMMENT '成绩等级',
    `gpa` DECIMAL(3,2) DEFAULT NULL COMMENT '绩点',
    `exam_type` VARCHAR(20) DEFAULT '正常' COMMENT '考试类型',
    `status` TINYINT DEFAULT 0 COMMENT '状态（0未发布1已发布）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_course_offer` (`student_id`, `course_offer_id`, `exam_type`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_semester_id` (`semester_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 考试安排表
DROP TABLE IF EXISTS `edu_exam`;
CREATE TABLE `edu_exam` (
    `id` BIGINT NOT NULL COMMENT '考试ID',
    `semester_id` BIGINT NOT NULL COMMENT '学期ID',
    `course_offer_id` BIGINT NOT NULL COMMENT '开课ID',
    `exam_type` VARCHAR(20) DEFAULT NULL COMMENT '考试类型',
    `exam_time` DATETIME DEFAULT NULL COMMENT '考试时间',
    `exam_location` VARCHAR(100) DEFAULT NULL COMMENT '考试地点',
    `duration` INT DEFAULT 120 COMMENT '考试时长（分钟）',
    `seat_count` INT DEFAULT NULL COMMENT '座位数',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_semester_course` (`semester_id`, `course_offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试安排表';

-- 课表视图（基于开课表）
DROP TABLE IF EXISTS `edu_schedule`;
CREATE TABLE `edu_schedule` (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `course_offer_id` BIGINT NOT NULL COMMENT '开课ID',
    `semester_id` BIGINT NOT NULL COMMENT '学期ID',
    `day_of_week` TINYINT DEFAULT NULL COMMENT '星期几（1-7）',
    `start_section` TINYINT DEFAULT NULL COMMENT '开始节次',
    `end_section` TINYINT DEFAULT NULL COMMENT '结束节次',
    `weeks` VARCHAR(200) DEFAULT NULL COMMENT '上课周次（如1-8,10-16）',
    `classroom` VARCHAR(50) DEFAULT NULL COMMENT '教室',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_semester` (`student_id`, `semester_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课表';

-- =============================================
-- 4. 生活服务相关表
-- =============================================

-- 一卡通消费记录表
DROP TABLE IF EXISTS `life_card_transaction`;
CREATE TABLE `life_card_transaction` (
    `id` BIGINT NOT NULL COMMENT '交易ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `card_no` VARCHAR(30) DEFAULT NULL COMMENT '卡号',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '交易金额',
    `balance` DECIMAL(10,2) DEFAULT NULL COMMENT '余额',
    `transaction_type` TINYINT DEFAULT 1 COMMENT '类型（1消费2充值3退款）',
    `merchant_name` VARCHAR(100) DEFAULT NULL COMMENT '商户名称',
    `merchant_no` VARCHAR(30) DEFAULT NULL COMMENT '商户号',
    `transaction_time` DATETIME DEFAULT NULL COMMENT '交易时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_transaction_time` (`transaction_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='一卡通消费记录表';

-- 报修工单表
DROP TABLE IF EXISTS `life_repair_order`;
CREATE TABLE `life_repair_order` (
    `id` BIGINT NOT NULL COMMENT '工单ID',
    `order_no` VARCHAR(30) NOT NULL COMMENT '工单编号',
    `user_id` BIGINT NOT NULL COMMENT '报修人ID',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '报修类别',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '报修地点',
    `description` TEXT COMMENT '问题描述',
    `images` JSON COMMENT '图片附件',
    `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT DEFAULT 0 COMMENT '状态（0待处理1处理中2已完成3已关闭）',
    `handler_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handle_remark` TEXT COMMENT '处理备注',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

-- 场地预约表
DROP TABLE IF EXISTS `life_venue_booking`;
CREATE TABLE `life_venue_booking` (
    `id` BIGINT NOT NULL COMMENT '预约ID',
    `venue_id` BIGINT NOT NULL COMMENT '场地ID',
    `user_id` BIGINT NOT NULL COMMENT '预约人ID',
    `booking_date` DATE NOT NULL COMMENT '预约日期',
    `start_time` TIME NOT NULL COMMENT '开始时间',
    `end_time` TIME NOT NULL COMMENT '结束时间',
    `purpose` VARCHAR(200) DEFAULT NULL COMMENT '用途',
    `attendees` INT DEFAULT NULL COMMENT '参加人数',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT DEFAULT 0 COMMENT '状态（0待审批1已通过2已拒绝3已取消）',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_venue_date` (`venue_id`, `booking_date`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地预约表';

-- 场地信息表
DROP TABLE IF EXISTS `life_venue`;
CREATE TABLE `life_venue` (
    `id` BIGINT NOT NULL COMMENT '场地ID',
    `venue_name` VARCHAR(100) NOT NULL COMMENT '场地名称',
    `venue_type` VARCHAR(50) DEFAULT NULL COMMENT '场地类型',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '位置',
    `capacity` INT DEFAULT NULL COMMENT '容量',
    `facilities` VARCHAR(500) DEFAULT NULL COMMENT '设施',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `open_time` TIME DEFAULT NULL COMMENT '开放时间',
    `close_time` TIME DEFAULT NULL COMMENT '关闭时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0关闭1开放）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地信息表';

-- 失物招领表
DROP TABLE IF EXISTS `life_lost_found`;
CREATE TABLE `life_lost_found` (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '发布人ID',
    `type` TINYINT NOT NULL COMMENT '类型（1失物2招领）',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `description` TEXT COMMENT '描述',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '物品类别',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '丢失/捡到地点',
    `lost_time` DATETIME DEFAULT NULL COMMENT '丢失/捡到时间',
    `images` JSON COMMENT '图片',
    `contact_way` VARCHAR(100) DEFAULT NULL COMMENT '联系方式',
    `status` TINYINT DEFAULT 0 COMMENT '状态（0进行中1已解决）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type_status` (`type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='失物招领表';

-- =============================================
-- 5. 通知公告相关表
-- =============================================

-- 公告表
DROP TABLE IF EXISTS `notice_announcement`;
CREATE TABLE `notice_announcement` (
    `id` BIGINT NOT NULL COMMENT '公告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `notice_type` TINYINT DEFAULT 1 COMMENT '类型（1学校2学院3班级4个人）',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID（学院公告用）',
    `publish_user_id` BIGINT DEFAULT NULL COMMENT '发布人ID',
    `publish_user_name` VARCHAR(50) DEFAULT NULL COMMENT '发布人姓名',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否需要已读回执',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0草稿1已发布2已撤回）',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_notice_type` (`notice_type`),
    KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 用户消息表
DROP TABLE IF EXISTS `notice_message`;
CREATE TABLE `notice_message` (
    `id` BIGINT NOT NULL COMMENT '消息ID',
    `user_id` BIGINT NOT NULL COMMENT '接收人ID',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `msg_type` TINYINT DEFAULT 1 COMMENT '消息类型（1系统2业务3待办）',
    `biz_type` VARCHAR(50) DEFAULT NULL COMMENT '业务类型',
    `biz_id` BIGINT DEFAULT NULL COMMENT '业务ID',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表';

-- =============================================
-- 6. 社区相关表
-- =============================================

-- 帖子表
DROP TABLE IF EXISTS `community_post`;
CREATE TABLE `community_post` (
    `id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '发帖人ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
    `images` JSON COMMENT '图片',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶',
    `is_essence` TINYINT DEFAULT 0 COMMENT '是否精华',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0隐藏1正常2审核中）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category` (`category`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 评论表
DROP TABLE IF EXISTS `community_comment`;
CREATE TABLE `community_comment` (
    `id` BIGINT NOT NULL COMMENT '评论ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '评论人ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID',
    `reply_user_id` BIGINT DEFAULT NULL COMMENT '回复人ID',
    `content` TEXT NOT NULL COMMENT '内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- =============================================
-- 7. 初始化数据
-- =============================================

-- 初始化管理员用户（密码：admin123）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `user_type`, `status`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 3, 1);

-- 初始化角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `sort_order`, `status`) VALUES
(1, '超级管理员', 'admin', 1, 1),
(2, '学生', 'student', 2, 1),
(3, '教师', 'teacher', 3, 1),
(4, '辅导员', 'counselor', 4, 1),
(5, '学院管理员', 'college_admin', 5, 1);

-- 初始化用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 初始化字典类型
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`) VALUES
(1, '用户类型', 'sys_user_type', 1),
(2, '性别', 'sys_sex', 1),
(3, '状态', 'sys_status', 1),
(4, '公告类型', 'notice_type', 1),
(5, '报修类别', 'repair_category', 1);

-- 初始化字典数据
INSERT INTO `sys_dict_data` (`id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(1, 1, '学生', '1', 'sys_user_type', 1),
(2, 2, '教师', '2', 'sys_user_type', 1),
(3, 3, '管理员', '3', 'sys_user_type', 1),
(4, 4, '校友', '4', 'sys_user_type', 1),
(5, 1, '未知', '0', 'sys_sex', 1),
(6, 2, '男', '1', 'sys_sex', 1),
(7, 3, '女', '2', 'sys_sex', 1),
(8, 1, '禁用', '0', 'sys_status', 1),
(9, 2, '正常', '1', 'sys_status', 1);

-- =============================================
-- 完成！
-- =============================================
