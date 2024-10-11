package com.shenzhen.dai.controller;

import com.shenzhen.dai.common.constant.ArticleConstants;
import com.shenzhen.dai.model.article.dtos.ArticleHomeDto;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.service.ApArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 文章入口
 * @author: daiyifan
 * @create: 2024-10-09 16:25
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 加载首页
     *
     * @param dto
     * @return
     */
    @PostMapping("/load/")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
    }

    /**
     * 加载更多
     *
     * @param dto
     * @return
     */
    @PostMapping("/loadmore/")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
    }

    /**
     * 加载最新
     *
     * @param dto
     * @return
     */
    @PostMapping("/loadnew/")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_NEW, dto);
    }
}
