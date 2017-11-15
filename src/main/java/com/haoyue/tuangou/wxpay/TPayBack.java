package com.haoyue.tuangou.wxpay;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by LiJia on 2017/11/14.
 */
public class TPayBack {


    /**
     * 申请退款
     * @return
     */
    @RequestMapping(value = "/refund")
    @Transactional
    public Object refund(String id, String user,String appId,String mchId,String key) {
        Map<String,Object> result = new HashMap<String,Object>();
        String currTime = TPayBackUtil.getCurrTime();
        String nonceStr = UUIDHexGenerator.generate();
        String outRefundNo = "订单单号";
        String outTradeNo = "订单单号";


        DecimalFormat df = new DecimalFormat("######0");
        String fee = String.valueOf(df.format(0));
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
        packageParams.put("transaction_id", "支付成功交易单号");//微信生成的订单号，在支付通知中有返回
        String sign = TPayBackUtil.createSign_ChooseWXPay("utf-8",packageParams,key);

        String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        String xmlParam="<xml>"+
                "<appid>"+appId+"</appid>"+
                "<mch_id>"+mchId+"</mch_id>"+
                "<nonce_str>"+nonceStr+"</nonce_str>"+
                "<op_user_id>"+mchId+"</op_user_id>"+
                "<out_refund_no>"+outRefundNo+"</out_refund_no>"+
                "<out_trade_no>"+outTradeNo+"</out_trade_no>"+
                "<refund_fee>"+fee+"</refund_fee>"+
                "<total_fee>"+fee+"</total_fee>"+
                "<transaction_id>"+"支付成功交易单号"+"</transaction_id>"+
                "<sign>"+sign+"</sign>"+
                "</xml>";
        String resultStr = TPayBackUtil.post(refundUrl, xmlParam);
        //解析结果
        try {
            Map map =  TPayBackUtil.doXMLParse(resultStr);
            System.out.println("退款结果");
            for (Object name:map.keySet()){
                System.out.println(name+"===="+map.get(name));
            }
            String returnCode = map.get("return_code").toString();
            if(returnCode.equals("SUCCESS")){
                String resultCode = map.get("result_code").toString();
                if(resultCode.equals("SUCCESS")){

                    result.put("status", "success");
                }else{
                    result.put("status", "fail");
                }
            }else{
                result.put("status", "fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail");
        }
        return result;
    }





}
