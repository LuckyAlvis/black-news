package com.shenzhen.dai.common.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
//@ConfigurationProperties(prefix = "aliyun")
public class GreenTextScan {

    private String accessKeyId;
    private String secret;

    public Map greeTextScan(String content) throws Exception {
        return new HashMap<>();
    }

}
