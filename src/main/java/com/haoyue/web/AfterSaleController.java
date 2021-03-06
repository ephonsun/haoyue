package com.haoyue.web;

import com.aliyuncs.exceptions.ClientException;
import com.haoyue.Exception.MyException;
import com.haoyue.pojo.*;
import com.haoyue.service.*;
import com.haoyue.untils.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by LiJia on 2017/9/20.
 * 售后,退货，退款
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
    @Autowired
    private SellerService sellerService;


    //http://localhost:8080/after-sale/save?oid=133&openId=1111&sellerId=1&pics=图片地址&type=[1 退货/退款 2 退款 ]&reason=原因&desc=退款说明&phone=电话
    @RequestMapping("/save")
    public Result save(String oid, AfterSale afterSale) {
        Order order = orderService.findOne(Integer.parseInt(oid));
        if (order.getIsApplyReturn()){
            return new Result(true, Global.already_apply_payback, null, null);
        }
        // 判断订单是否已经签收
        String code = order.getDeliver().getDcode();
        boolean flag = Kuaidi.getStatus(code);
        if (flag == false && afterSale.getType().equals("1")) {
            //未签收
            return new Result(true, Global.not_receive, null, null);
        }
        //检验 sellerId
        if (order.getSellerId() != Integer.parseInt(afterSale.getSellerId())) {
            return new Result(true, Global.have_no_right, null, null);
        }
        //判断之前是否撤销过申请，每个订单撤销后不能再次申请
        List<AfterSale> list = afterSaleService.findByOrderId(order.getId());
        if (list != null && list.size() !=0) {
            for (AfterSale item:list){
                if (item.getCancel()){
                    return new Result(true, Global.not_can_apply_cancel_true, null, null);
                }
            }
        }
        //更新订单是否申请退换货
        order.setIsApplyReturn(true);
        orderService.update(order);
        afterSale.setOrder(order);
        afterSale.setCreateDate(new Date());
        afterSale.setTotalPrice(order.getTotalPrice());
        afterSale.setIsAgree("0");//等待卖家处理
        afterSale.setPages("1");

        return new Result(false, Global.do_success, afterSaleService.save(afterSale, true), null);
    }


    //   卖家后台待退款列表 /after-sale/list?pageNumber=页数，从0开始&sellerId=卖家ID(&state=0 待处理/ 1 同意 / 2 拒绝)
    //  买家查看退款订单  /after-sale/list?pageNumber=页数，从0开始&sellerId=卖家ID&openId=123444
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, Global.do_success, afterSaleService.list(map, pageNumber, pageSize), null);
    }


    //http://localhost:8080/after-sale/deal?id=退款记录ID&sellerId=卖家ID&isAgree=yes/no&response=卖家回复&receiveAddress=收货地址
    @RequestMapping("/deal")
    public Result deal(String id, String sellerId, String isAgree, String response, String receiveAddress) {
        if (!StringUtils.isNullOrBlank(receiveAddress)&&receiveAddress.equals("auto")){
            receiveAddress=sellerService.findOne(Integer.parseInt(sellerId)).getReceiveAddress();
        }
        AfterSale afterSale = afterSaleService.findOne(id);
        if (!sellerId.equals(afterSale.getSellerId())) {
            return new Result(true, Global.have_no_right, null, null);
        }
        String str="";
        //同意
        if (isAgree.equals("yes")) {
            afterSale.setIsAgree("1");
            if (afterSale.getType().equals("1")) {
                afterSale.setPages("4");
                str = "卖家同意退款/退货申请，退货地址：" + receiveAddress;
                if (response.equals("auto")){
                    str = "系统默认处理同意退款/退货申请，退货地址：" + receiveAddress;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 7);
                afterSale.setEndDeliverDate(calendar.getTime());
                afterSale.setReceiveAddress(receiveAddress);
            }
            if (afterSale.getType().equals("2")) {
                afterSale.setPages("2");
                afterSale.setProcess(3);
                afterSale.setSuccessDate(new Date());
                str = "卖家同意退款申请," + afterSale.getOrder().getTotalPrice() + "元已经退回买家账户";
                if (response.equals("auto")){
                    response="系统默认处理 ";
                    str="系统默认处理同意退款："+ afterSale.getOrder().getTotalPrice() + "元已经退回买家账户";
                }
            }

        } else {
            //不同意
            afterSale.setIsAgree("2");
            if (afterSale.getType().equals("1")) {
                afterSale.setPages("6");
                str = "卖家拒绝退货/退款申请，理由：" + response;
            }
            if (afterSale.getType().equals("2")) {
                afterSale.setPages("3");
                str = "卖家拒绝退款申请，理由：" + response;
            }
        }
        afterSale.setResponse(response);
        afterSale.setDealDate(new Date());
        afterSaleService.update(afterSale, str, false);
        //退款
        if (afterSale.getType().equals("2")) {
            //退款  更新dictionarys表数据
            if (afterSale.getIsAgree().equals("1")) {
                Order order = afterSale.getOrder();
                //拼接参数
                String param = "sellerId=" + order.getSellerId() + "&oid=" + order.getId() + "&fe=" + afterSale.getTotalPrice() * 100;
                //退款请求
                String result = HttpRequest.sendGet("https://www.cslapp.com/payback/do", param);
                System.out.println("after-sale-result:" + result);
                if (!result.equalsIgnoreCase("fail")) {
                    addTemplate(order, afterSale.getFormId());
                    //更新订单退款状态
                    order.setIsApplyReturnFinsh(true);
                    orderService.update(order);
                }
            }
        }
        return new Result(false, Global.do_success, null, null);
    }

    //  /after-sale/findone?sellerId=卖家ID&id=退款记录Id
    @RequestMapping("/findone")
    public Result findOne(String id, String sellerId) {
        AfterSale afterSale = afterSaleService.findOne(id);
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

    //  /after-sale/del?sellerId=卖家ID&id=申请退款记录Id
    @RequestMapping("/del")
    public Result cancel(String id, String sellerId, String openId) {
        AfterSale afterSale = afterSaleService.findOne(id);
        if (!sellerId.equals(afterSale.getSellerId())) {
            return new Result(true, Global.have_no_right, null, null);
        }
        if (StringUtils.isNullOrBlank(openId)) {
            afterSale.setActive(false);
        }
        if (!StringUtils.isNullOrBlank(openId)) {
            afterSale.setActive_buyer(false);
        }
        afterSaleService.update(afterSale, null, false);
        return new Result(false, Global.do_success, null, null);
    }

    //  /after-sale/message_list?id=申请退款记录Id&sellerId=12
    @RequestMapping("/message_list")
    public Result messageList(int id, String sellerId) {
        return new Result(false, Global.do_success, afterSaleService.messageList(id), null);
    }


    //  /after-sale/sendback?id=申请退款记录Id&dname=快递名&dcode=单号&openId=1
    @RequestMapping("/sendback")
    public void sendback(String id, String dname, String dcode) {
        AfterSale afterSale = afterSaleService.findOne(id);
        afterSale.setDname(dname);
        afterSale.setDcode(dcode);
        afterSale.setPages("5");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 10);
        afterSale.setEndReceiveDate(calendar.getTime());
        String str = "买家已经退货，物流名：" + dname + " ,物流单号:" + dcode;
        //短信通知商家
        Seller seller=sellerService.findOne(Integer.parseInt(afterSale.getSellerId()));
        Customer customer=customerService.findByOpenId(afterSale.getOpenId(),afterSale.getSellerId());
        try {
            SendCode.sendSms4(seller.getSellerPhone(),customer.getWxname());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        afterSaleService.update(afterSale, str, true);
    }


    //  /after-sale/cancel?id=申请退款记录Id&openId=123
    @RequestMapping("/cancel")
    public Result cancel(String id, String openId) {
        AfterSale afterSale = afterSaleService.findOne(id);
        if (!openId.equals(afterSale.getOpenId())) {
            return new Result(true, Global.have_no_right, null, null);
        }
        //单个订单只能撤销一次
        List<AfterSale> list = afterSaleService.findByOrderId(afterSale.getOrder().getId());
        if (list != null && list.size() != 0) {
            for (AfterSale afterSale1 : list) {
                if (afterSale1.getCancel() == true) {
                    return new Result(true, Global.already_cancel, null, null);
                }
            }
        }
        afterSale.setCancel(true);
        afterSaleService.update(afterSale, null, false);

        return new Result(false, Global.do_success, null, null);
    }

    //  /after-sale/update?pics=图片地址&type=[1 退货/退款 2 退款 ]&reason=原因&desc=退款说明&phone=电话&openId=122&id=121212
    @RequestMapping("/update")
    public Result update(AfterSale afterSale, String again) {

        AfterSale afterSale1 = afterSaleService.findOne(afterSale.getId() + "");
        //判断之前是否撤销过申请，每个订单撤销后不能再次申请
        List<AfterSale> list = afterSaleService.findByOrderId(afterSale1.getOrder().getId());
        if (list != null && list.size() !=0) {
            for (AfterSale item:list){
                if (item.getCancel()){
                    return new Result(true, Global.not_can_apply_cancel_true, null, null);
                }
            }
        }
        afterSale1.setType(afterSale.getType());
        afterSale1.setDescs(afterSale.getDescs());
        afterSale1.setReason(afterSale.getReason());
        afterSale1.setPhone(afterSale.getPhone());
        afterSale1.setPics(afterSale.getPics());
        afterSale1.setFormId(afterSale.getFormId());
        if (!StringUtils.isNullOrBlank(again)) {
            afterSale1.setCreateDate(new Date());
            afterSale1.setPages("1");
            afterSale1.setResponse(null);
            afterSale1.setDealDate(null);
            afterSale1.setIsAgree("0");
        }
        afterSaleService.update(afterSale1, afterSale1.getDescs(), true);
        return new Result(false, Global.do_success, null, null);
    }


    //  /after-sale/receive?id=申请记录ID&sellerId=12
    @RequestMapping("/receive")
    public Result receive(String id,String auto) {
        AfterSale afterSale = afterSaleService.findOne(id);
        afterSale.setReceive(true);
        String str = "卖家已经确认收货并执行系统退款";
        if (!StringUtils.isNullOrBlank(auto)&&auto.equals("yes")){
            str = "系统自动确认收货并执行系统退款";
        }
        afterSale.setProcess(2);
        afterSale.setPages("7");
        afterSale.setSuccessDate(new Date());
        afterSaleService.update(afterSale, str,false);
        //拼接参数
        String param = "sellerId=" + afterSale.getSellerId() + "&oid=" + afterSale.getOrder().getId() + "&fe=" + afterSale.getTotalPrice() * 100;
        //退款请求
        String result = HttpRequest.sendGet("https://www.cslapp.com/payback/do", param);
        System.out.println("after-sale-result:" + result);
        if (!result.equalsIgnoreCase("fail")) {
            addTemplate(afterSale.getOrder(), afterSale.getFormId());
            //更新订单退货退款状态
            afterSale.getOrder().setIsApplyReturnFinsh(true);
            orderService.update(afterSale.getOrder());
        }
        return new Result(false, Global.do_success, null, null);
    }

    //  /after-sale/update_price?id=申请记录ID&totalPrice=修改后的总价&openId=221
    @RequestMapping("/update_price")
    public Result updatePrice(String id, double totalPrice, String openId) {
        AfterSale afterSale = afterSaleService.findOne(id);
        String str = "买家修改了退款金额：" + totalPrice + " ,原退款金额：" + afterSale.getTotalPrice();
        afterSale.setTotalPrice(totalPrice);
        afterSaleService.update(afterSale, str, true);
        return new Result(false, Global.do_success, null, null);
    }

    public void getTemplate(Template template, String formId,Seller seller) {
        //模板信息通知用户
        //获取 access_token
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid="+seller.getAppId()+"&secret="+seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);

        //发送模板信息
        String form_id = formId;
        template.setForm_id(form_id);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token + "&form_id=" + form_id;
        String result = CommonUtil.httpRequest(url, "POST", template.toJSON());
        System.out.println("高级版-申请退款-通知结果：" + result);
    }

    public void addTemplate(Order order, String formId) {
        Seller seller=sellerService.findOne(order.getSellerId());
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
        template.setTemplateId(seller.getPayback_template());
        template.setTemplateParamList(list);
        template.setTopColor("#000000");
        String pagePath="pages/index/index";
        if(order.getSellerId()==3||order.getSellerId()==14){
            pagePath="pages/goods/goods";
        }
        template.setPage(pagePath);
        template.setToUser(customerService.findOpenIdById(order.getCustomerId() + ""));
        getTemplate(template, formId,seller);
    }

}
