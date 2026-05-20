package com.zhihui.common.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private int code;

    /** 消息 */
    private String msg;

    /** 数据 */
    private T data;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> R<T> ok() {
        return new R<>(SUCCESS_CODE, "success", null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS_CODE, "success", data);
    }

    public static <T> R<T> ok(String msg, T data) {
        return new R<>(SUCCESS_CODE, msg, data);
    }

    /**
     * 失败响应
     */
    public static <T> R<T> fail() {
        return new R<>(FAIL_CODE, "操作失败", null);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(FAIL_CODE, msg, null);
    }

    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, msg, null);
    }

    /**
     * 未授权响应
     */
    public static <T> R<T> unauthorized() {
        return new R<>(UNAUTHORIZED_CODE, "未授权，请先登录", null);
    }

    public static <T> R<T> unauthorized(String msg) {
        return new R<>(UNAUTHORIZED_CODE, msg, null);
    }

    /**
     * 禁止访问响应
     */
    public static <T> R<T> forbidden() {
        return new R<>(FORBIDDEN_CODE, "没有权限访问", null);
    }

    public static <T> R<T> forbidden(String msg) {
        return new R<>(FORBIDDEN_CODE, msg, null);
    }

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return SUCCESS_CODE == this.code;
    }
}
