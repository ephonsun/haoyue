package com.haoyue.interceptor;

import com.haoyue.service.SellerService;
import com.haoyue.tuangou.Exception.TMyException;
import com.haoyue.tuangou.pojo.TUserSale;
import com.haoyue.tuangou.service.TUserSaleService;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by LiJia on 2017/11/17.
 * 拼多多拦截器
 */
public class BaseInterceptor3 implements HandlerInterceptor {

    @Autowired
    private TUserSaleService userSaleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //判断 saleId 和 openId 是否为空
        String saleId=request.getParameter("saleId");
        String openId=request.getParameter("openId");
        String url=request.getRequestURI();
//        if(!url.contains("/tuan")){
//            return true;
//        }
        if (StringUtils.isNullOrBlank(saleId)&&StringUtils.isNullOrBlank(openId)&&!url.contains("/tusersale")&&!url.contains("/uploadPic")){
            throw new TMyException(TGlobal.saleid_openid_isnull);
        }
        //检验onlinecode
        if (!StringUtils.isNullOrBlank(request.getParameter("onlinecode"))) {
            //注入service
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            userSaleService = (TUserSaleService) factory.getBean("TUserSaleService");
            List<TUserSale> list= userSaleService.findByOnlineCode(request.getParameter("onlinecode"));
            if (list==null||list.size()==0){
                throw new TMyException(TGlobal.out_line);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
