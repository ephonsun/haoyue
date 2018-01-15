package com.haoyue.wxpay;

/**
 * Created by LiJia on 2018/1/4.
 */
import com.haoyue.pojo.*;
import com.haoyue.pojo.Dictionary;
import com.haoyue.service.DictionaryService;
import com.haoyue.service.OrderService;
import com.haoyue.service.PayBackDealService;
import com.haoyue.service.SellerService;
import com.haoyue.untils.Global;
import com.haoyue.untils.Result;
import com.haoyue.untils.SendCode;
import com.haoyue.untils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by LiJia on 2017/11/14.
 * 退款操作
 */
@RestController
@RequestMapping("/payback")
public class PayBack {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayDealService payDealService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private PayBackDealService payBackDealService;

    /**
     * 申请退款
     *
     * @return
     */
    // https://www.cslapp.com/payback/do?sellerId=3&out_trade_no=14878628022017111615363948647073&transaction_id=4200000037201711165033853205&fe=1
    //  /payback/do?sellerId=3&oid=订单Id&fe=订单总价
    @RequestMapping("/do")
    public Object refund(String sellerId,String oid, String fe) {
        //获取卖家的基本信息
        Seller sale = sellerService.findOneById(Integer.parseInt(sellerId));
        String appId = sale.getAppId();
        String mchId = sale.getMchId();
        String key = sale.getKey1();
        Map<String, Object> result = new HashMap<String, Object>();
        String nonceStr = UUIDHexGenerator.generate();
        //获取订单的 out_trade_no
        Order order = orderService.findOne(Integer.parseInt(oid));
        String out_trade_no = order.getOrderCode();
        String outRefundNo = out_trade_no;
        String outTradeNo = out_trade_no;

        //根据 out_trade_no 获取 transaction_id
        String transaction_id = payDealService.findByOut_trade_no(out_trade_no).getTransaction_id();
        String payprice=payDealService.findByOut_trade_no(out_trade_no).getTotal_fee();

        DecimalFormat df = new DecimalFormat("######0");
        String fee = String.valueOf(df.format(Double.valueOf(fe)));
        //订单价和支付价不同，以支付价为准
        if (!fee.equals(payprice)){
            fee=payprice;
        }
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("appid", appId);
        packageParams.put("mch_id", mchId);//微信支付分配的商户号
        packageParams.put("nonce_str", nonceStr);//随机字符串，不长于32位
        packageParams.put("op_user_id", mchId);//操作员帐号, 默认为商户号
        //out_refund_no只能含有数字、字母和字符_-|*@
        packageParams.put("out_refund_no", outRefundNo);//商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
        packageParams.put("out_trade_no", outTradeNo);//商户侧传给微信的订单号32位
        packageParams.put("refund_fee", fee);//退款金额
        packageParams.put("total_fee", fee);//总金额
        packageParams.put("transaction_id", transaction_id);//微信生成的订单号，在支付通知中有返回
        String sign = PayBackUtil.createSign_ChooseWXPay("utf-8", packageParams, key);

        String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        String xmlParam = "<xml>" +
                "<appid>" + appId + "</appid>" +
                "<mch_id>" + mchId + "</mch_id>" +
                "<nonce_str>" + nonceStr + "</nonce_str>" +
                "<op_user_id>" + mchId + "</op_user_id>" +
                "<out_refund_no>" + outRefundNo + "</out_refund_no>" +
                "<out_trade_no>" + outTradeNo + "</out_trade_no>" +
                "<refund_fee>" + fee + "</refund_fee>" +
                "<total_fee>" + fee + "</total_fee>" +
                "<transaction_id>" + transaction_id + "</transaction_id>" +
                "<sign>" + sign + "</sign>" +
                "</xml>";
        //调用退款操作
        String resultStr = PayBackUtil.post(refundUrl, xmlParam,sale.getFile_payback(),mchId);
        //解析结果
        try {
            Map map = PayBackUtil.doXMLParse(resultStr);
            System.out.println("退款结果");
            for (Object name : map.keySet()) {
                System.out.println(name + "====" + map.get(name));
                if (name.equals("result_code")){
                    if (((String)map.get(name)).contains("FAIL")){
                        //  短信通知卖家退款失败,同一个卖家,一天只通知一次
                        List<String> list= Global.sendsms3;
                        if (!list.contains(sellerId)){
                            SendCode.sendSms3("18715161200");
                            Global.sendsms3.add(sellerId);
                        }
                    }
                }
            }
            // err_code_des====基本账户余额不足，请充值后重新发起
            String returnCode = map.get("return_code").toString();
            if (returnCode.equals("SUCCESS")) {
                String resultCode = map.get("result_code").toString();
                if (resultCode.equals("SUCCESS")) {
                    //更新数据表交易额
                    Dictionary dictionarys= dictionaryService.findByDateAndSellerId(StringUtils.getYMD(order.getCreateDate()),Integer.parseInt(sellerId));
                    dictionarys.setTurnover(dictionarys.getTurnover()-order.getTotalPrice());
                    dictionaryService.update(dictionarys);
                    result.put("status", "success");
                } else {
                    result.put("status", "fail");
                    return "fail";
                }
            } else {
                result.put("status", "fail");
                return "fail";
            }
            //保存退款信息
            backNotify(map,sellerId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail");
            return "fail";
        }
        return result;
    }


    public void backNotify(Map map,String sellerId) {
        PayBackDeal payBackDeal=new PayBackDeal();
        payBackDeal.setAppid(String.valueOf(map.get("appid")));
        payBackDeal.setDate(new Date());
        payBackDeal.setOut_trade_no(String.valueOf(map.get("out_trade_no")));
        payBackDeal.setMch_id(String.valueOf(map.get("mch_id")));
        payBackDeal.setRefund_fee(String.valueOf(map.get("refund_fee")));
        payBackDeal.setRefund_id(String.valueOf(map.get("refund_id")));
        payBackDeal.setTransaction_id(String.valueOf(map.get("transaction_id")));
        payBackDeal.setResult_code(String.valueOf(map.get("result_code")));
        payBackDeal.setSettlement_refund_fee(String.valueOf(map.get("settlement_refund_fee")));
        payBackDeal.setSellerId(sellerId);
        payBackDealService.save(payBackDeal);
    }

    // 退款列表
    @RequestMapping("/list")
    public Result list(String saleId, @RequestParam(defaultValue = "0") int pageNumber){
        // pageSize=10 固定
        Iterable<PayBackDeal> iterable= payBackDealService.list(saleId,pageNumber);
        return new Result(false,Global.do_success,iterable,null);
    }

}
