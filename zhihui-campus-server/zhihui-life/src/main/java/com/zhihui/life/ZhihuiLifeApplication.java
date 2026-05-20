package com.zhihui.life;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 生活服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.zhihui.life", "com.zhihui.common"})
@EnableDiscoveryClient
public class ZhihuiLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiLifeApplication.class, args);
        System.out.println("====================================");
        System.out.println("智慧校园生活服务启动成功!");
        System.out.println("====================================");
    }
}
