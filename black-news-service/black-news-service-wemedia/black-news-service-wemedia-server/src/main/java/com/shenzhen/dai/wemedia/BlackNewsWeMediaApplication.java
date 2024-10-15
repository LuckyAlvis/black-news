package com.shenzhen.dai.wemedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.shenzhen.dai")
@EnableHystrix
@EnableFeignClients(basePackages = "com.shenzhen.dai")
public class BlackNewsWeMediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlackNewsWeMediaApplication.class, args);
    }
}
