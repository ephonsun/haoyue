package com.haoyue.interceptor;

import com.haoyue.Exception.MyException;
import com.haoyue.untils.Global;
import com.haoyue.untils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class BaseInterceptor implements HandlerInterceptor {

    //private final static Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

//        仅供nginx调试使用
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String,String[]> map=request.getParameterMap();
        System.out.println("访问时间  "+StringUtils.getstrDate());
        System.out.println("访问路径  "+request.getRequestURI());
        //logger.info("访问时间  "+StringUtils.getstrDate());
        //logger.info("访问路径  "+request.getRequestURI());

        for (String key:map.keySet()){
             System.out.println(key+"="+map.get(key)[0]);
           // logger.info(key+"="+map.get(key)[0]);
        }
            String url=request.getRequestURI();
            if (Global.urls().contains(url)||url.contains("super-admin")||url.contains("leave-message")){
                return true;
            }
            else {
                String token=request.getParameter("token");
                String openId=request.getParameter("openId");
                String sellerId=request.getParameter("sellerId");
                if (StringUtils.isNullOrBlank(token)&&StringUtils.isNullOrBlank(openId)&&StringUtils.isNullOrBlank(sellerId)){
                    throw  new MyException(Global.user_unlogin,null,101);
                }
            }
        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

       // System.out.println(">>>MyInterceptor2>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
       /// System.out.println(">>>MyInterceptor2>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}

