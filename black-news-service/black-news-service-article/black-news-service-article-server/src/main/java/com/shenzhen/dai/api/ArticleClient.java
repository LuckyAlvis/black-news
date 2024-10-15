package com.shenzhen.dai.api;

import com.shenzhen.dai.apis.article.IArticleClient;
import com.shenzhen.dai.model.article.dtos.ArticleDto;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.service.ApArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApArticleService apArticleService;

    @Override
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }

}
