package com.shenzhen.dai.model.user.dtos;

import lombok.Data;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-08 18:23
 */
@Data
public class LoginDto {
    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;
}
