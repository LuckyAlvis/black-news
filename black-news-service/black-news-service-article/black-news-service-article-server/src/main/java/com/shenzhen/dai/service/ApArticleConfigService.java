package com.shenzhen.dai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shenzhen.dai.model.article.pojos.ApArticleConfig;

import java.util.Map;

public interface ApArticleConfigService extends IService<ApArticleConfig> {

    /**
     * 修改文章配置
     *
     * @param map
     */
    public void updateByMap(Map map);
}
