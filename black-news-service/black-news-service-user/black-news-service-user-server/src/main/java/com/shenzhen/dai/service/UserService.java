package com.shenzhen.dai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.user.dtos.LoginDto;
import com.shenzhen.dai.model.user.pojos.ApUser;

public interface UserService extends IService<ApUser> {

    ResponseResult<?> login(LoginDto loginDto);
}
