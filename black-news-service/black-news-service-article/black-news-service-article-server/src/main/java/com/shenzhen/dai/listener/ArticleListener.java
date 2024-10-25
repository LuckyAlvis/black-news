package com.shenzhen.dai.listener;

import com.alibaba.fastjson.JSON;
import com.shenzhen.dai.common.constant.WmNewsMessageConstants;
import com.shenzhen.dai.service.ApArticleConfigService;
import com.shuwei.dai.ObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-25 13:33
 */

@Component
@Slf4j
public class ArticleListener implements ObjectService {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @KafkaListener(topics = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC)
    public void onMessage(String message) {
        if (notBlank(message)) {
            Map map = JSON.parseObject(message, Map.class);
            apArticleConfigService.updateByMap(map);
            log.info("article端文章配置修改，articleId={}", map.get("articleId"));
        }
    }
}
