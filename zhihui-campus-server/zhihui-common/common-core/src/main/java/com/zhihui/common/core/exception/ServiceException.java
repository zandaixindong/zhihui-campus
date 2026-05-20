package com.zhihui.common.core.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 错误码 */
    private int code;

    /** 错误消息 */
    private String message;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
    }
}
