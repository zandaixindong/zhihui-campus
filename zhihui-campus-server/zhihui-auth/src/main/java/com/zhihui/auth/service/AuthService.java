package com.zhihui.auth.service;

import com.zhihui.auth.dto.LoginRequest;
import com.zhihui.auth.dto.LoginResponse;
import com.zhihui.common.core.domain.LoginUser;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout(String token);

    /**
     * 刷新Token
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 获取当前登录用户信息
     */
    LoginUser getUserInfo(String token);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 注册用户
     */
    void register(LoginRequest request);
}
