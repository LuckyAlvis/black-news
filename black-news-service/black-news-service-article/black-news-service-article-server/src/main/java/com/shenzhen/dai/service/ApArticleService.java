package com.shenzhen.dai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shenzhen.dai.model.article.dtos.ArticleDto;
import com.shenzhen.dai.model.article.dtos.ArticleHomeDto;
import com.shenzhen.dai.model.article.pojos.ApArticle;
import com.shenzhen.dai.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 保存app端相关文章
     *
     * @param dto
     * @return
     */
    ResponseResult saveArticle(ArticleDto dto);

    /**
     * 加载文章列表
     *
     * @param dto
     * @param type 1 加载更多   2 加载最新
     * @return
     */
    ResponseResult load(Short type, ArticleHomeDto dto);

}
