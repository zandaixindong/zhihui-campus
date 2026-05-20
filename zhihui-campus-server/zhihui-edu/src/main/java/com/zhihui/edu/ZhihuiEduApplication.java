package com.zhihui.edu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 教务服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.zhihui.edu.mapper")
@ComponentScan(basePackages = {"com.zhihui.edu", "com.zhihui.common"})
public class ZhihuiEduApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiEduApplication.class, args);
        System.out.println("========== 教务服务启动成功 ==========");
    }
}
