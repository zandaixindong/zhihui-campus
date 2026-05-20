package com.zhihui.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 */
@Getter
@AllArgsConstructor
public enum UserType {

    STUDENT(1, "学生"),
    TEACHER(2, "教师"),
    ADMIN(3, "管理员"),
    ALUMNI(4, "校友");

    private final int code;
    private final String desc;

    public static UserType fromCode(int code) {
        for (UserType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown user type code: " + code);
    }
}
