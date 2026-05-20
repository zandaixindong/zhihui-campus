package com.zhihui.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zhihui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /** 用户ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户名（学号/工号） */
    private String username;

    /** 密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 用户类型（1学生2教师3管理员4校友） */
    private Integer userType;

    /** 学院ID */
    private Long collegeId;

    /** 专业ID */
    private Long majorId;

    /** 班级 */
    private String className;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像 */
    private String avatar;

    /** 性别（0未知1男2女） */
    private Integer sex;

    /** 状态（0禁用1正常） */
    private Integer status;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 最后登录IP */
    private String lastLoginIp;

    /** 删除标志（0存在1删除） */
    @TableLogic
    private Integer deleted;
}
