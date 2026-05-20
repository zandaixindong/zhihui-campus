package com.zhihui.common.security.filter;

import com.zhihui.common.core.domain.LoginUser;
import com.zhihui.common.security.utils.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LoginUser loginUser = null;

        try {
            loginUser = tokenService.getLoginUser(request);
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
        }

        if (loginUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 验证Token是否有效
            tokenService.verifyToken(loginUser);

            // 构建权限列表
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (loginUser.getRoles() != null) {
                loginUser.getRoles().forEach(role ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                );
            }
            if (loginUser.getPermissions() != null) {
                loginUser.getPermissions().forEach(permission ->
                        authorities.add(new SimpleGrantedAuthority(permission))
                );
            }

            // 设置认证信息
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中获取Token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenService.getHeader());
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }
}
