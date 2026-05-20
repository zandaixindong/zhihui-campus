package com.zhihui.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 学工服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.zhihui.student", "com.zhihui.common"})
@EnableDiscoveryClient
public class ZhihuiStudentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiStudentApplication.class, args);
        System.out.println("====================================");
        System.out.println("智慧校园学工服务启动成功!");
        System.out.println("====================================");
    }
}
