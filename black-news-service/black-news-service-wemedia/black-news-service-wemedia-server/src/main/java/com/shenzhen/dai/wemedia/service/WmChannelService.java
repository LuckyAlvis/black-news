package com.shenzhen.dai.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查询所有频道
     *
     * @return
     */
    public ResponseResult findAll();


}
