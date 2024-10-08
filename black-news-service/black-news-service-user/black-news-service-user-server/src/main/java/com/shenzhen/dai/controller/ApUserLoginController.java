package com.shenzhen.dai.controller;

import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.user.dtos.LoginDto;
import com.shenzhen.dai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-08 18:12
 */
@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login_auth")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

}
