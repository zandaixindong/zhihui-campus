package com.zhihui.common.core.utils;

import com.zhihui.common.core.domain.LoginUser;
import com.zhihui.common.core.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        throw new ServiceException("未登录或登录已过期");
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取当前用户角色
     */
    public static java.util.Set<String> getRoles() {
        return getLoginUser().getRoles();
    }

    /**
     * 获取当前用户权限
     */
    public static java.util.Set<String> getPermissions() {
        return getLoginUser().getPermissions();
    }

    /**
     * 是否管理员
     */
    public static boolean isAdmin() {
        return getLoginUser().isAdmin();
    }

    /**
     * 密码加密
     */
    public static String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * 密码验证
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
