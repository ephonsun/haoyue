package com.tuangou.web;

import com.aliyuncs.exceptions.ClientException;
import com.tuangou.pojo.TUserSale;
import com.tuangou.service.TUserSaleService;
import com.tuangou.utils.StringUtils;
import com.tuangou.utils.TGlobal;
import com.tuangou.utils.TResult;
import com.tuangou.utils.TSendCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/2.
 */
@RestController
public class TUserSaleController {

    @Autowired
    private TUserSaleService tUserSaleService;

    // http://localhost:8080/tuan/tusersale/save?appId=1&name=abc&pass=123&email=123@qq.com&phone=123&authority=[0 1 2]
    public void save(TUserSale tUserSale){
        tUserSaleService.save(tUserSale);
    }

    // http://localhost:8080/tuan/tusersale/findone?[name=11&phone=12&email=123@qq.com 参数可选,至少一个]
    public TResult findOne(TUserSale tUserSale){
        Iterable<TUserSale> iterable=tUserSaleService.findOne(tUserSale);
        iterable=hidepass(iterable);
       return new TResult(false, TGlobal.do_success,iterable);
    }

    // http://localhost:8080/tuan/tusersale/list?[name=11&phone=12&email=123@qq.com 参数可选,无参数也可]
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Iterable<TUserSale> iterable= tUserSaleService.list(map,pageNumber,pageSize);
        return new TResult(false, TGlobal.do_success,iterable);
    }

    // http://localhost:8080/tuan/tusersale/login?name=用户名、手机、邮箱&pass=1234
    public TResult login(TUserSale tUserSale){
        TUserSale sale=tUserSaleService.login(tUserSale);
        if (sale==null){
            return new TResult(false, TGlobal.tusersale_isnull,null);
        }else {
            sale.setPass("******");
            return new TResult(false, TGlobal.do_success,sale);
        }
    }

    // http://localhost:8080/tuan/tusersale/update?id=1&email=123@qq.com&phone=123&pass=123&name=123&lunbo=123412
    public TResult update(TUserSale tUserSale){
        tUserSaleService.update(tUserSale);
        return new TResult(false, TGlobal.do_success,null);
    }

    // http://localhost:8080/tuan/tusersale/checkpass?id=1&pass=123
    public TResult checkoldpass(TUserSale sale){
        String oldpass=sale.getPass();
        sale.setPass(null);
        Iterable<TUserSale> iterable=tUserSaleService.findOne(sale);
        Iterator<TUserSale> iterator=iterable.iterator();
        TUserSale tUserSale=iterator.next();
        if (!oldpass.equals(tUserSale.getPass())){
            return new TResult(false,TGlobal.oldpass_not_right,null);
        }else {
            return new TResult(false,TGlobal.oldpass_right,null);
        }
    }

    public Iterable<TUserSale> hidepass(Iterable<TUserSale> iterable){
        Iterator<TUserSale> iterator=iterable.iterator();
        while (iterator.hasNext()){
            iterator.next().setPass("*******");
        }
        return iterable;
    }

    // http://localhost:8080/tuan/tusersale/get_phonecode?id=1
    public TResult getphonecode(TUserSale sale){
        Iterable<TUserSale> iterable=tUserSaleService.findOne(sale);
        Iterator<TUserSale> iterator=iterable.iterator();
        TUserSale tUserSale=iterator.next();
        String phonecode=StringUtils.getPhoneCode();
        try {
            TSendCode.sendSms(tUserSale.getPhone(), phonecode);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        // 1 可在 TUserSale 加入 phonecode 字段，获取验证码后刷新该字段，再对用户输入的验证码进行对比
        // 2 phonecode 进行加密传输
        return new TResult(false,TGlobal.do_success,phonecode);
    }






}
