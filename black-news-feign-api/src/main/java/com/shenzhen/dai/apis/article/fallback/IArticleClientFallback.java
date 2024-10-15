package com.shenzhen.dai.apis.article.fallback;

import com.shenzhen.dai.apis.article.IArticleClient;
import com.shenzhen.dai.model.article.dtos.ArticleDto;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * nacos中增加 todo 目前未生效
 * feign:
 *   # 开启feign对hystrix熔断降级的支持
 *   hystrix:
 *     enabled: true
 *   # 修改调用超时时间
 *   client:
 *     config:
 *       default:
 *         connectTimeout: 2000
 *         readTimeout: 2000
 */
@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, "获取数据失败");
    }
}
