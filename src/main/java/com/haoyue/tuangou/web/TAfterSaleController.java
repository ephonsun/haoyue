package com.haoyue.tuangou.web;


import com.haoyue.tuangou.pojo.*;
import com.haoyue.tuangou.service.TAfterSaleService;
import com.haoyue.tuangou.service.TOrdersService;
import com.haoyue.tuangou.service.TuanOrdersService;
import com.haoyue.tuangou.utils.*;
import com.haoyue.tuangou.wxpay.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/9/20.
 * 售后
 */

@RestController
@RequestMapping("/tuan/after-sale")
public class TAfterSaleController {

    @Autowired
    private TAfterSaleService afterSaleService;
    @Autowired
    private TOrdersService ordersService;
    @Autowired
    private TuanOrdersService tuanOrdersService;


    //http://localhost:8080/tuan/after-sale/save?ordercode=订单号&openId=1111&saleId=1&message=理由内容&pics=图片地址
    @RequestMapping("/save")
    public TResult save(String ordercode, TAfterSale afterSale) {
        if (ordercode.startsWith("888")) {
            TOrders orders = ordersService.findByCode(ordercode);
            if (!orders.getSaleId().equals(afterSale.getSaleId())) {
                return new TResult(true, TGlobal.have_no_right, null);
            }
            afterSale.setOrder(orders);
            orders.setIsapplyreturn(true);
            ordersService.update(orders);
        } else if (ordercode.startsWith("666")) {
            TuanOrders tuanOrders = tuanOrdersService.findByCode(ordercode);
            if (!tuanOrders.getSaleId().equals(afterSale.getSaleId())) {
                return new TResult(true, TGlobal.have_no_right, null);
            }
            afterSale.setTuanOrders(tuanOrders);
            tuanOrders.setIsapplyreturn(true);
        }

        afterSale.setCreateDate(new Date());
        afterSale.setIsAgree("0");//等待卖家处理
        return new TResult(false, TGlobal.do_success, afterSaleService.save(afterSale));
    }


    //   卖家后台待退款列表  /tuan/after-sale/list?pageNumber=页数，从0开始&sellerId=卖家ID(&state=0 待处理/ 1 同意 / 2 拒绝)
    //  买家查看退款订单   /tuan/after-sale/list?pageNumber=页数，从0开始&sellerId=卖家ID&openId=123444
    @RequestMapping("/list")
    public TResult list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new TResult(false, TGlobal.do_success, afterSaleService.list(map, pageNumber, pageSize));
    }


    //http://localhost:8080/tuan/after-sale/deal?id=退款记录ID&saleId=卖家ID&isAgree=yes/no
    @RequestMapping("/deal")
    public TResult deal(Integer id, String saleId, String isAgree) {
        TAfterSale afterSale = afterSaleService.findOne(id);
        if (!saleId.equals(afterSale.getSaleId())) {
            return new TResult(true, TGlobal.have_no_right, null);
        }
        //同意 更新dictionarys表数据
        if (isAgree.equals("yes")) {
            afterSale.setIsAgree("1");
        } else {
            //不同意
            afterSale.setIsAgree("2");
        }
        afterSaleService.save(afterSale);
        //退款
        if (afterSale.getIsAgree().equals("1")) {
            if (afterSale.getTuanOrders() != null) {
                //拼接参数
                String param = "saleId=" + afterSale.getTuanOrders().getSaleId() + "&ordercode=" + afterSale.getTuanOrders().getCode() + "&fe=" + afterSale.getTuanOrders().getTotalPrice() * 100;
                //退款请求
                String result = HttpRequest.sendGet("https://www.cslapp.com/tuan/payback/do", param);
                System.out.println("高级版-申请退款-result：" + result);
                //退款成功 发送模板信息
                if (!result.equalsIgnoreCase("fail")) {
                    TuanOrders tuanOrders = afterSale.getTuanOrders();
                    addTemplate(tuanOrders.gettProducts().getPname(), tuanOrders.getTotalPrice(), afterSale.getFormId(), afterSale.getOpenId());
                }
            }
            if (afterSale.getOrder() != null) {
                TOrders order = afterSale.getOrder();
                //拼接参数
                String param = "saleId=" + order.getSaleId() + "&ordercode=" + order.getCode() + "&fe=" + order.getTotalPrice() * 100;
                //退款请求
                String result = HttpRequest.sendGet("https://www.cslapp.com/tuan/payback/do", param);
                System.out.println("高级版-申请退款-result：" + result);
                //退款成功 发送模板信息
                if (!result.equalsIgnoreCase("fail")) {
                    TOrders tOrders = afterSale.getOrder();
                    addTemplate(tOrders.gettProducts().getPname(), tOrders.getTotalPrice(), afterSale.getFormId(), afterSale.getOpenId());
                }
            }
        }
        return new TResult(false, TGlobal.do_success, null);
    }


    //  /tuan/after-sale/uploadPics?saleId=卖家ID&multipartFiles=文件
    @RequestMapping("/uploadPics")
    public TResult uploadPics(MultipartFile[] multipartFiles, String saleId) {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (TGlobal.object7) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                TOSSClientUtil ossClientUtil = new TOSSClientUtil();
                try {
                    String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                    stringBuffer.append(TGlobal.aliyun_href + uploadUrl);
                    stringBuffer.append(",");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new TResult(false, TGlobal.do_success, stringBuffer.toString());
    }

    public void getTemplate(Template template, String formId) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        //发送模板信息
        String form_id = formId;
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("高级版-申请退款-通知结果：" + result);
    }

    public void addTemplate(String pname, double totalPrice, String formId, String open_id) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(pname);
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(String.valueOf(totalPrice));
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(StringUtils.formDateToStr(new Date()));
        list.add(templateResponse3);

        Template template = new Template();
        template.setTemplateId("xnwdwzgeOoKA9_ljfPAe7GfAH_pzmRucfAiWjEWSDQA");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(open_id);
        getTemplate(template, formId);
    }

}
