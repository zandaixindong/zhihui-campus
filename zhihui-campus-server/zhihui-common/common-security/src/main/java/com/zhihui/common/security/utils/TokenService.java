package com.zhihui.common.security.utils;

import com.zhihui.common.core.constant.Constants;
import com.zhihui.common.core.domain.LoginUser;
import com.zhihui.common.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token工具类
 */
@Data
@Component
public class TokenService {

    @Value("${token.secret:abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMN}")
    private String secret;

    @Value("${token.expireTime:720}")
    private int expireTime;

    @Value("${token.header:Authorization}")
    private String header;

    private static final long MILLIS_SECOND = 1000;
    private static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final long MILLIS_DAY = 24 * 60 * MILLIS_MINUTE;

    private static final long MILLIS = 20 * MILLIS_MINUTE;

    private final RedisService redisService;

    private SecretKey key;

    public TokenService(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建Token
     */
    public String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    /**
     * 解析Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 生成Token
     */
    public String createToken(LoginUser loginUser) {
        String userKey = UUID.randomUUID().toString().replace("-", "");
        loginUser.setToken(userKey);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, userKey);
        return createToken(claims);
    }

    /**
     * 刷新Token有效期
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据UUID将LoginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取登录用户
     */
    public LoginUser getLoginUser(String token) {
        try {
            if (token != null && !token.isEmpty()) {
                Claims claims = parseToken(token);
                String userKey = claims.get(Constants.LOGIN_USER_KEY, String.class);
                return redisService.get(getTokenKey(userKey));
            }
        } catch (Exception e) {
            // Token无效
        }
        return null;
    }

    /**
     * 删除登录用户缓存
     */
    public void deleteLoginUser(String token) {
        if (token != null && !token.isEmpty()) {
            String userKey = getTokenKey(token);
            redisService.delete(userKey);
        }
    }

    /**
     * 设置用户代理信息
     */
    public void setUserAgent(LoginUser loginUser) {
        // 可以在此处记录用户代理信息（浏览器、操作系统、IP等）
    }

    /**
     * 从Token中获取登录用户Key
     */
    public String getTokenKey(String token) {
        return Constants.LOGIN_USER_KEY + token;
    }

    /**
     * 验证Token有效期，自动刷新
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS) {
            refreshToken(loginUser);
        }
    }
}
