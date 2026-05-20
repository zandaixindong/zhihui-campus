package com.zhihui.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 通知服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.zhihui.notice", "com.zhihui.common"})
@EnableDiscoveryClient
public class ZhihuiNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiNoticeApplication.class, args);
        System.out.println("====================================");
        System.out.println("智慧校园通知服务启动成功!");
        System.out.println("====================================");
    }
}
