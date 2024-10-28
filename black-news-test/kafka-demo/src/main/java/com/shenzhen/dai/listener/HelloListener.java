package com.shenzhen.dai.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelloListener {

    @KafkaListener(topics = "demo-topic")
    public void onMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            System.out.println(message);
        }
    }
}
