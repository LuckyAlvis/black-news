package com.shenzhen.dai.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shenzhen.dai.common.exception.CustomException;
import com.shenzhen.dai.mapper.UserMapper;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.common.enums.AppHttpCodeEnum;
import com.shenzhen.dai.model.user.dtos.LoginDto;
import com.shenzhen.dai.model.user.pojos.ApUser;
import com.shenzhen.dai.service.UserService;
import com.shuwei.dai.ObjectService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-08 18:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, ApUser> implements UserService, ObjectService {
    @Override
    public ResponseResult<?> login(LoginDto loginDto) {
        if (isBlank(loginDto.getPhone()) || isBlank(loginDto.getPassword())) {
            throw new CustomException(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        // 1.1正常登录 用户名和密码
        // 1.2根据手机号查询用户信息
        ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, loginDto.getPhone()));
        if (isNull(dbUser)) {
            throw new CustomException(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        String salt = dbUser.getSalt();
        String passwd = salt + loginDto.getPassword();
        // 1.3比对密码
        if (eq(DigestUtils.md5DigestAsHex(passwd.getBytes()), DigestUtils.md5DigestAsHex((salt + loginDto.getPassword()).getBytes()))) {
            // 1.4返回数据(JWT token)
//            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            String token = "token_str";
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            dbUser.setSalt(" ");
            dbUser.setPassword("");
            map.put("user", dbUser);
            return ResponseResult.okResult(map);
        }
        // 2 游客登录
        return ResponseResult.okResult("游客登录");

    }
}
