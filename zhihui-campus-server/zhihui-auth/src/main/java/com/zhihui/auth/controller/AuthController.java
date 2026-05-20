package com.zhihui.auth.controller;

import com.zhihui.auth.dto.LoginRequest;
import com.zhihui.auth.dto.LoginResponse;
import com.zhihui.auth.service.AuthService;
import com.zhihui.common.core.domain.LoginUser;
import com.zhihui.common.core.domain.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户登录、登出、注册等接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return R.ok(response);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        authService.logout(token);
        return R.ok();
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/user-info")
    public R<LoginUser> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        LoginUser loginUser = authService.getUserInfo(token);
        return R.ok(loginUser);
    }

    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public R<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return R.ok(response);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public R<Void> changePassword(@RequestParam Long userId,
                                  @RequestParam String oldPassword,
                                  @RequestParam String newPassword) {
        authService.changePassword(userId, oldPassword, newPassword);
        return R.ok();
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody LoginRequest request) {
        authService.register(request);
        return R.ok();
    }

    /**
     * 测试接口
     */
    @Operation(summary = "测试接口")
    @GetMapping("/test")
    public R<String> test() {
        return R.ok("认证服务正常");
    }
}
