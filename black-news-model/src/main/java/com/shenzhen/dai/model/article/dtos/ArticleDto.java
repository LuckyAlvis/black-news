package com.shenzhen.dai.model.article.dtos;

import com.shenzhen.dai.model.article.pojos.ApArticle;
import lombok.Data;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-14 16:20
 */
@Data
public class ArticleDto extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}
