package com.zhihui.auth.service.impl;

import com.zhihui.auth.dto.LoginRequest;
import com.zhihui.auth.dto.LoginResponse;
import com.zhihui.auth.entity.SysUser;
import com.zhihui.auth.mapper.SysUserMapper;
import com.zhihui.auth.service.AuthService;
import com.zhihui.common.core.domain.LoginUser;
import com.zhihui.common.core.exception.ServiceException;
import com.zhihui.common.core.utils.SecurityUtils;
import com.zhihui.common.security.utils.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final TokenService tokenService;

    public AuthServiceImpl(SysUserMapper userMapper, TokenService tokenService) {
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 查询用户
        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 验证密码
        if (!SecurityUtils.matchesPassword(request.getPassword(), user.getPassword())) {
            throw new ServiceException("密码错误");
        }

        // 验证状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new ServiceException("账号已被禁用");
        }

        // 构建登录用户信息
        LoginUser loginUser = buildLoginUser(user);

        // 生成Token
        String token = tokenService.createToken(loginUser);

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("用户登录成功: {}", user.getUsername());

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn((long) tokenService.getExpireTime() * 60)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .userType(user.getUserType())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenService.deleteLoginUser(token);
        log.info("用户登出成功");
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // TODO: 实现刷新Token逻辑
        throw new ServiceException("功能暂未实现");
    }

    @Override
    public LoginUser getUserInfo(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        LoginUser loginUser = tokenService.getLoginUser(token);
        if (loginUser == null) {
            throw new ServiceException("登录已过期，请重新登录");
        }
        return loginUser;
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 验证旧密码
        if (!SecurityUtils.matchesPassword(oldPassword, user.getPassword())) {
            throw new ServiceException("旧密码错误");
        }

        // 更新密码
        user.setPassword(SecurityUtils.encryptPassword(newPassword));
        userMapper.updateById(user);

        log.info("用户修改密码成功: {}", user.getUsername());
    }

    @Override
    public void register(LoginRequest request) {
        // 检查用户名是否已存在
        SysUser existingUser = userMapper.selectByUsername(request.getUsername());
        if (existingUser != null) {
            throw new ServiceException("用户名已存在");
        }

        // 创建新用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
        user.setStatus(1);
        user.setUserType(1); // 默认学生
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);

        log.info("用户注册成功: {}", user.getUsername());
    }

    /**
     * 构建登录用户信息
     */
    private LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRealName(user.getRealName());
        loginUser.setUserType(user.getUserType());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setPhone(user.getPhone());
        loginUser.setEmail(user.getEmail());
        loginUser.setCollegeId(user.getCollegeId());
        loginUser.setClassName(user.getClassName());

        // TODO: 从数据库查询用户角色和权限
        loginUser.setRoles(new HashSet<>());
        loginUser.setPermissions(new HashSet<>());

        return loginUser;
    }
}
