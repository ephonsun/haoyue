package com.haoyue.tuangou.Exception;

/**
 * Created by LiJia on 2017/8/25.
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
