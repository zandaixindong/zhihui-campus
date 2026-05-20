package com.zhihui.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统管理服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.zhihui.system", "com.zhihui.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zhihui.system.feign")
public class ZhihuiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiSystemApplication.class, args);
        System.out.println("====================================");
        System.out.println("智慧校园系统管理服务启动成功!");
        System.out.println("====================================");
    }
}
