package com.haoyue.web;

import com.haoyue.Exception.MyException;
import com.haoyue.pojo.*;
import com.haoyue.service.AfterSaleService;
import com.haoyue.service.CustomerService;
import com.haoyue.service.DictionaryService;
import com.haoyue.service.OrderService;
import com.haoyue.untils.*;
import org.aspectj.weaver.ast.Or;
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
@RequestMapping("/after-sale")
public class AfterSaleController {

    @Autowired
    private AfterSaleService afterSaleService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;

    //http://localhost:8080/after-sale/save?oid=133&openId=1111&sellerId=1&message=理由内容&pics=图片地址
    @RequestMapping("/save")
    public Result save(String oid, AfterSale afterSale) {
        Order order = orderService.findOne(Integer.parseInt(oid));
        if (order.getSellerId() != Integer.parseInt(afterSale.getSellerId())) {
            return new Result(true, Global.have_no_right, null, null);
        }
        if(order.getIsApplyReturn()){
            return new Result(true, Global.already_apply_payback, null, null);
        }
        //更新订单是否申请退换货
        order.setIsApplyReturn(true);
        orderService.update(order);
        afterSale.setOrder(order);
        afterSale.setCreateDate(new Date());
        afterSale.setIsAgree("0");//等待卖家处理
        return new Result(false, Global.do_success, afterSaleService.save(afterSale), null);
    }


    //   卖家后台待退款列表 /after-sale/list?pageNumber=页数，从0开始&sellerId=卖家ID(&state=0 待处理/ 1 同意 / 2 拒绝)
    //  买家查看退款订单  /after-sale/list?pageNumber=页数，从0开始&sellerId=卖家ID&openId=123444
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, Global.do_success, afterSaleService.list(map, pageNumber, pageSize), null);
    }


    //http://localhost:8080/after-sale/deal?id=退款记录ID&sellerId=卖家ID&isAgree=yes/no
    @RequestMapping("/deal")
    public Result deal(String id, String sellerId, String isAgree) {
        AfterSale afterSale = afterSaleService.findOne(id);
        if (!sellerId.equals(afterSale.getSellerId())) {
            return new Result(true, Global.have_no_right, null, null);
        }
        //同意 更新dictionarys表数据
        if (isAgree.equals("yes")) {
            afterSale.setIsAgree("1");
        } else {
            //不同意
            afterSale.setIsAgree("2");
        }
        afterSaleService.update(afterSale);
        //退款
        if (afterSale.getIsAgree().equals("1")) {
            Order order = afterSale.getOrder();
            //拼接参数
            String param = "saleId=" + order.getSellerId() + "&oid=" + order.getId() + "&fe=" + order.getTotalPrice() * 100;
            //退款请求
            String result = HttpRequest.sendGet("https://www.cslapp.com/payback/do", param);
            System.out.println("after-sale-result:" + result);
            if (!result.equalsIgnoreCase("fail")) {
                addTemplate(order,afterSale.getFormId());
            }
        }
        return new Result(false, Global.do_success, null, null);
    }


    //  /after-sale/uploadPics?sellerId=卖家ID&id=退款记录Id
    @RequestMapping("/findone")
    public Result findOne(String id,String sellerId){
        AfterSale afterSale= afterSaleService.findOne(id);
        return new Result(false, Global.do_success, afterSale, null);
    }


    //  /after-sale/uploadPics?sellerId=卖家ID&multipartFiles=文件
    @RequestMapping("/uploadPics")
    public Result uploadPics(MultipartFile[] multipartFiles, Integer sellerId) throws MyException {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (Global.object4) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MultipartFile multipartFile : multipartFiles) {
                OSSClientUtil ossClientUtil = new OSSClientUtil();
                String uploadUrl = ossClientUtil.uploadImg2Oss(multipartFile);
                stringBuffer.append(Global.aliyun_href + uploadUrl);
                stringBuffer.append(",");
            }
        }
        return new Result(false, Global.do_success, stringBuffer.toString(), null);
    }

    public void getTemplate(Template template, String formId) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxe46b9aa1b768e5fe&secret=8bcdb74a9915b5685fa0ec37f6f25b24";
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);

        //发送模板信息
        String form_id = formId;
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("高级版-申请退款-通知结果："+result);
    }

    public void addTemplate(Order order,String formId) {

        List<TemplateResponse> list = new ArrayList<>();
        TemplateResponse templateResponse1 = new TemplateResponse();
        templateResponse1.setColor("#000000");
        templateResponse1.setName("keyword1");
        templateResponse1.setValue(order.getProducts().get(0).getPname());
        list.add(templateResponse1);

        TemplateResponse templateResponse2 = new TemplateResponse();
        templateResponse2.setColor("#000000");
        templateResponse2.setName("keyword2");
        templateResponse2.setValue(order.getTotalPrice() + "");
        list.add(templateResponse2);

        TemplateResponse templateResponse3 = new TemplateResponse();
        templateResponse3.setColor("#000000");
        templateResponse3.setName("keyword3");
        templateResponse3.setValue(StringUtils.formDateToStr(new Date()));
        list.add(templateResponse3);

        Template template = new Template();
        template.setTemplateId("-0Za6kJa3GroTxYOVLdCpGtun1KflJUKRld29bm9Wx0");
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        template.setPage("pages/index/index");
        template.setToUser(customerService.findOpenIdById(order.getCustomerId() + ""));
        getTemplate(template, formId);
    }

}
