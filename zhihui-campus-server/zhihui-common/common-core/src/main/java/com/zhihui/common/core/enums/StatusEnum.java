package com.zhihui.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    DISABLE(0, "禁用"),
    ENABLE(1, "正常"),
    DELETED(2, "删除");

    private final int code;
    private final String desc;

    public static StatusEnum fromCode(int code) {
        for (StatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
