package com.zhihui.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.zhihui.auth", "com.zhihui.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zhihui.auth.feign")
public class ZhihuiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiAuthApplication.class, args);
        System.out.println("====================================");
        System.out.println("智慧校园认证服务启动成功!");
        System.out.println("====================================");
    }
}
