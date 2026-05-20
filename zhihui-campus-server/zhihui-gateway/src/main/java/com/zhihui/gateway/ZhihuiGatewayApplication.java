package com.zhihui.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ZhihuiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiGatewayApplication.class, args);
        System.out.println("====================================");
        System.out.println("智慧校园网关服务启动成功!");
        System.out.println("====================================");
    }
}
