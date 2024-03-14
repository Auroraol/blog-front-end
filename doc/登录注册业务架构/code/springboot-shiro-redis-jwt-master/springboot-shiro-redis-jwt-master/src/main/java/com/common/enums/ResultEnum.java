package com.common.enums;


import lombok.Getter;

/**
 * 返回状态枚举类
 */
@Getter
public enum ResultEnum {

    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION(100, "未知异常"),
    /**
     * 请求方式不支持
     */
    REQ_METHOD_NOT_SUPPORT(101,"请求方式不支持"),
    /**
     * 格式错误
     */
    FORMAT_ERROR(102, "参数格式错误"),
    /**
     * 文件格式错误
     */
    FILE_FORMAT_ERROR(103,"文件格式错误"),
    FILE_PATH_ERROR(105,"文件上传路径错误"),
    FILE_NAME_NOT_NULL(106,"文件名不可为空"),
    /**
     * 参数类型不匹配
     */
    ARGUMENT_TYPE_MISMATCH(104, "参数类型不匹配"),


    /**
     * 添加失败
     */
    ADD_ERROR(2000, "添加失败"),

    /**
     * 更新失败
     */
    UPDATE_ERROR(2001, "更新失败"),

    /**
     * 删除失败
     */
    DELETE_ERROR(2002, "删除失败"),

    /**
     * 查找失败
     */
    GET_ERROR(2003, "查询失败，数据可能不存在"),
    /**
     * 导入失败
     */
    IMPORT_ERROR(2004,"导入失败"),


    /**
     * 用户名或密码错误
     * */
    USER_PWD_ERROR(1000, "用户名或密码错误"),
    /**
     * 用户不存在
     * */
    USER_NOT_ERROR(1001, "用户不存在"),
    /** 登录超时，请重新登录 */
    LOGIN_TIME_OUT(1002,"登录超时，请重新登录"),
    /** 用户未登录，请进行登录 */
    USER_NOT_LOGIN(1003,"用户未登录，请进行登录"),
    /** 账号锁定 */
    USER_LOCK(1004,"账号锁定中"),

    /**
     * 非法令牌
     */
    ILLEGAL_TOKEN(5000,"非法令牌"),
    /**
     * 其他客户端登录
     */
    OTHER_CLIENT_LOGIN(5001,"其他客户端登录"),
    /**
     * 令牌过期
     */
    TOKEN_EXPIRED(5002,"令牌过期"),
    /**
     * 权限不足
     */
    SHIRO_ERROR(403,"权限不足");
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通过状态码获取枚举对象
     * @param code 状态码
     * @return 枚举对象
     */
    public static ResultEnum getByCode(int code){
        for (ResultEnum resultEnum : ResultEnum.values()) {
            if(code == resultEnum.getCode()){
                return resultEnum;
            }
        }
        return null;
    }

}

