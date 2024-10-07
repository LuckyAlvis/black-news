package com.shenzhen.dai.common.exception;

import com.shenzhen.dai.model.common.enums.AppHttpCodeEnum;

public class CustomException extends RuntimeException {
    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum) {
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return this.appHttpCodeEnum;
    }

}
