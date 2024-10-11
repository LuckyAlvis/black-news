package com.shenzhen.dai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shenzhen.dai.model.article.dtos.ArticleHomeDto;
import com.shenzhen.dai.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDto dto, @Param("type") Short type);

}
