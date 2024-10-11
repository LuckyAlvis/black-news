package com.shenzhen.dai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BlackNewsUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlackNewsUserApplication.class, args);
    }
}
