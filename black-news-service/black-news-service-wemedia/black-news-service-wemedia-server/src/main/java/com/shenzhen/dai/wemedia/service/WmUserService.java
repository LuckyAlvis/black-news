package com.shenzhen.dai.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.wemedia.dtos.WmLoginDto;
import com.shenzhen.dai.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {

    /**
     * 自媒体端登录
     *
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}
