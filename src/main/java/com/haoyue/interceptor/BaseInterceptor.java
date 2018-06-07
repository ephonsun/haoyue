package com.haoyue.interceptor;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.Seller;
import com.haoyue.service.SellerService;
import com.haoyue.untils.Global;
import com.haoyue.untils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
* 全局拦截器
* 这个拦截器主要拦截 禾才小程序和禾才卖家后台，如果是其他的项目会跳转到对应的拦截器。根据url的开始字符串。
* 在这里我输出打印了请求路径、请求时间、请求参数等等，方便后期查看日志
* 每一条请求(除去已经过滤的路径)都需要携带参数 sellerId 或 openId 或 token  否则无法通过拦截
* 当online_code不为空时候，会去校验online_code的可用性，卖家每一次登录后台时候会刷新当前卖家对应的online_code值
* */

public class BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private SellerService sellerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 仅供nginx调试使用
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map<String, String[]> map = request.getParameterMap();
        System.out.println("访问时间  " + StringUtils.getstrDate());
        System.out.println("访问路径  " + request.getRequestURI());
        //访问携带参数
        for (String key : map.keySet()) {
            System.out.println(key + "=" + map.get(key)[0]);
        }
        String url = request.getRequestURI();
        //如果是拼多多直接跳过
        if (url.contains("/tuan")||url.contains("website")||url.contains("activity")){
            return true;
        }
        //注入service
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        sellerService = (SellerService) factory.getBean("SellerService");

        //当online_code不为空时候 校验online_code是否正确
        if (!request.getRequestURI().contains("login") && !request.getRequestURI().contains("super-admin") && !StringUtils.isNullOrBlank(request.getParameter("online_code")) && !sellerService.findByOnlineCode(request.getParameter("online_code"))) {
            throw new MyException(Global.seller_online, null, 102);
        }

        //校验是否携带参数token
        if (Global.urls().contains(url) || url.contains("super-admin") || url.contains("leave-message")||url.contains("uploadPics")) {
            return true;
        } else {
            String token = request.getParameter("token");
            String openId = request.getParameter("openId");
            String sellerId = request.getParameter("sellerId");
            if (StringUtils.isNullOrBlank(token) && StringUtils.isNullOrBlank(openId) && StringUtils.isNullOrBlank(sellerId)) {
                throw new MyException(Global.user_unlogin, null, 101);
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

