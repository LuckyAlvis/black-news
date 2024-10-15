package com.shenzhen.dai.apis.article;

import com.shenzhen.dai.apis.article.fallback.IArticleClientFallback;
import com.shenzhen.dai.model.article.dtos.ArticleDto;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-14 16:17
 */
@FeignClient(value = "black-news-article", fallback = IArticleClientFallback.class)
public interface IArticleClient {

    @PostMapping("/api/v1/article/save")
    ResponseResult saveArticle(@RequestBody ArticleDto dto);
}
