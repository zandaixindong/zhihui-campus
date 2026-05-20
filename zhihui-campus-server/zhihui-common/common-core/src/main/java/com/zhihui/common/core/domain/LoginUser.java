package com.zhihui.common.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * 登录用户信息
 */
@Data
@NoArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户名（学号/工号） */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 用户类型（1学生2教师3管理员） */
    private Integer userType;

    /** 头像 */
    private String avatar;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 学院ID */
    private Long collegeId;

    /** 学院名称 */
    private String collegeName;

    /** 专业ID */
    private Long majorId;

    /** 班级 */
    private String className;

    /** 角色集合 */
    private Set<String> roles;

    /** 权限集合 */
    private Set<String> permissions;

    /** Token */
    private String token;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    public LoginUser(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     * 是否管理员
     */
    public boolean isAdmin() {
        return userType != null && userType == 3;
    }

    /**
     * 是否学生
     */
    public boolean isStudent() {
        return userType != null && userType == 1;
    }

    /**
     * 是否教师
     */
    public boolean isTeacher() {
        return userType != null && userType == 2;
    }
}
