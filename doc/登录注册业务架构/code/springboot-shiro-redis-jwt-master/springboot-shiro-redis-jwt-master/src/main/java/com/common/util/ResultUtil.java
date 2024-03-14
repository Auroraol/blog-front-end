package com.common.util;

import com.common.enums.ResultEnum;
import com.common.vo.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * 返回数据工具类
 */
public class ResultUtil {

    /**
     * 私有化工具类 防止被实例化
     */
    private ResultUtil() {}

    /**
     * 成功
     * @param object 需要返回的数据
     * @return data
     */
    public static ResultVo success(Object object) {
        ResultVo result = new ResultVo();
        result.setCode(0);
        result.setMessage("ok");
        result.setData(object);
        return result;
    }

    /**
     * 成功
     * @param map 需要返回的数据
     * @return data
     */
    public static ResultVo success(Map<String, List> map) {
        ResultVo result = new ResultVo();
        result.setCode(0);
        result.setMessage("ok");
        result.setData(map);
        return result;
    }

    /**
     * 成功
     */
    public static ResultVo success(Integer code,String msg) {
        ResultVo result = new ResultVo();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    /**
     * 成功
     * @return 返回空
     */
    public static ResultVo success() {
        return success(null);
    }

    /**
     * 错误
     * @param resultEnum 错误枚举类
     * @return 错误信息
     */
    public static ResultVo error(ResultEnum resultEnum) {
        ResultVo result = new ResultVo();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMsg());
        return result;
    }

    /**
     * 错误
     * @param code 状态码
     * @param msg 消息
     * @return ResultBean
     */
    public static ResultVo error(Integer code, String msg) {
        ResultVo result = new ResultVo();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    /**
     * 错误
     * @param msg 错误信息
     * @return ResultBean
     */
    public static ResultVo error(String msg) {
        return error(-1, msg);
    }

}
