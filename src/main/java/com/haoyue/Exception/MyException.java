package com.haoyue.Exception;

/**
 * Created by LiJia on 2017/8/25.
 *
 * 自定义异常  errcode 是错误代码
 * 整个项目中基本没有用到，我都是通过 Result.message 抛出了异常信息
 *
 */
public class MyException extends Exception {

    private Integer errcode;

    public MyException(String message) {
        super(message);
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public MyException(String message, Throwable cause, Integer errcode) {
        super(message, cause);
        this.errcode = errcode;
    }
}
