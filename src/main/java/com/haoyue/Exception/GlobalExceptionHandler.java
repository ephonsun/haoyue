package com.haoyue.Exception;

import com.haoyue.tuangou.Exception.TMyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by LiJia on 2017/9/8.
 *
 * springboot  配置全局异常处理类
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //  当抛出的异常是 MyException(禾才小程序) 时候执行如下代码
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(e.getErrcode());
        r.setData(null);
        //r.setUrl(req.getRequestURL().toString());
        return r;
    }

    //  当抛出的异常是 TMyException(图实惠小程序) 时候执行如下代码
    @ExceptionHandler(value = TMyException.class)
    @ResponseBody
    public ErrorInfo<String> tjsonErrorHandler(HttpServletRequest req, TMyException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(e.getErrcode());
        r.setData(null);
        //r.setUrl(req.getRequestURL().toString());
        return r;
    }

}

