package com.artisankid.elementwar.controller.utils;

/**
 * Created by LiXiangYu on 2017/5/13.
 */
public enum ErrorEnum {
    Success(0, "success"),
    UseCache(1, "可以使用缓存数据"),
    ServerError(1000, "服务器错误"),
    PermissionError(2000, "权限错误（需要登录）"),
    PermissionScopeError(2001, "权限范围错误（超出用户权限）"),
    RequestParamLack(8000, "请求参数缺失"),
    RequestParamError(8001, "请求格式错误（请求参数格式错误，类型错误）"),

    AccessTokenCantNil(1000000, "access token不可为空"),
    AccessTokenNotExist(1000001, "access token不存在"),
    AccessTokenError(1000002, "access token错误（不匹配）"),
    AccessTokenExpired(1000003, "access token过期"),
    RefreshTokenCantNil(1000010, "refresh token不可为空"),
    RefreshTokenNotExist(1000011, "refresh token不存在"),
    RefreshTokenError(1000012, "refresh token错误（不匹配）"),
    RefreshTokenExpired(1000013, "refresh token过期"),
    OpenIDCantNil(1000020, "openID不可为空"),
    OpenIDNotExist(1000021, "openID不存在"),

    SecurityCodeSendError(1001000, "验证码发送失败"),
    SecurityCodeSent(1001001, "验证码已发送"),
    SecurityCodePrevented(1001002, "验证码获取太频繁"),
    SecurityCodeCantNil(1001010, "验证码不可为空"),
    SecurityCodeNotExist(1001011, "验证码不存在"),
    SecurityCodeError(1001012, "验证码错误（不匹配）"),
    SecurityCodeExpired(1001013, "验证码过期"),

    UsernameCantNil(1002000, "用户名不可为空"),
    UsernameInvalid(1002001, "用户名格式错误"),
    UsernameExist(1002002, "用户名已经存在（注册）"),
    UsernameNotExist(1002003, "用户名不存在（登录）"),

    EmailCantNil(1002010, "邮箱不可为空"),
    EmailInvalid(1002011, "邮箱格式错误"),
    EmailExist(1002012, "邮箱已经存在（注册）"),
    EmailNotExist(1002013, "邮箱不存在（登录）"),

    MobileCantNil(1002020, "手机号不可为空"),
    MobileInvalid(1002021, "手机号格式错误"),
    MobileExist(1002022, "手机号已经存在（注册）"),
    MobileNotExist(1002023, "手机号不存在（登录）"),

    PasswordCantNil(1002030, "密码不可为空"),
    PasswordInvalid(1002031, "密码格式错误"),
    PasswordError(1002032, "密码错误（登录）");

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
