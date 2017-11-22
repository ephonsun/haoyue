package com.haoyue.tuangou.wxpay;

import com.haoyue.pojo.Seller;
import com.haoyue.service.SellerService;
import com.haoyue.tuangou.pojo.TUserSale;
import com.haoyue.tuangou.pojo.TuanOrders;
import com.haoyue.tuangou.service.TPayBackDealService;
import com.haoyue.tuangou.service.TUserSaleService;
import com.haoyue.tuangou.service.TuanOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by LiJia on 2017/11/14.
 * 退款操作
 */
@RestController
@RequestMapping("/tuan/payback")
public class TPayBack {


    @Autowired
    private TUserSaleService tUserSaleService;
    @Autowired
    private TuanOrdersService tuanOrdersService;
    @Autowired
    private TPayDealService tPayDealService;
    @Autowired
    private TPayBackDealService tPayBackDealService;

    /**
     * 申请退款
     *
     * @return
     */
    // https://www.cslapp.com/tuan/payback/test?sellerId=3&out_trade_no=14878628022017111615363948647073&transaction_id=4200000037201711165033853205&fe=1
    @RequestMapping("/do")
    public Object refund(String saleId, String user, String oid, int fe) {
        //获取卖家的基本信息
        TUserSale sale = tUserSaleService.findOneById(Integer.parseInt(saleId));
        String appId = sale.getAppId();
        String mchId = sale.getMachId();
        String key = sale.getKey1();
        Map<String, Object> result = new HashMap<String, Object>();
        String nonceStr = UUIDHexGenerator.generate();
        //获取订单的 out_trade_no
        TuanOrders tuanOrders = tuanOrdersService.findOne(Integer.parseInt(oid));
        String out_trade_no = tuanOrders.getOut_trade_no();
        String outRefundNo = out_trade_no;
        String outTradeNo = out_trade_no;

        //根据 out_trade_no 获取 transaction_id
        String transaction_id = tPayDealService.findByOut_trade_no(out_trade_no).getTransaction_id();

        DecimalFormat df = new DecimalFormat("######0");
        String fee = String.valueOf(df.format(fe));
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
        String sign = TPayBackUtil.createSign_ChooseWXPay("utf-8", packageParams, key);

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
        String resultStr = TPayBackUtil.post(refundUrl, xmlParam);
        //解析结果
        try {
            Map map = TPayBackUtil.doXMLParse(resultStr);
            System.out.println("退款结果");
            for (Object name : map.keySet()) {
                System.out.println(name + "====" + map.get(name));
            }
            String returnCode = map.get("return_code").toString();
            if (returnCode.equals("SUCCESS")) {
                String resultCode = map.get("result_code").toString();
                if (resultCode.equals("SUCCESS")) {
                    result.put("status", "success");
                } else {
                    result.put("status", "fail");
                }
            } else {
                result.put("status", "fail");
            }
            //保存退款信息
            backNotify(map);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail");
        }
        return result;
    }


    public void backNotify(Map map) {
        TPayBackDeal payBackDeal=new TPayBackDeal();
        payBackDeal.setAppid(String.valueOf(map.get("appid")));
        payBackDeal.setDate(new Date());
        payBackDeal.setOut_trade_no(String.valueOf(map.get("out_trade_no")));
        payBackDeal.setMch_id(String.valueOf(map.get("mch_id")));
        payBackDeal.setRefund_fee(String.valueOf(map.get("refund_fee")));
        payBackDeal.setRefund_id(String.valueOf(map.get("refund_id")));
        payBackDeal.setTransaction_id(String.valueOf(map.get("transaction_id")));
        payBackDeal.setResult_code(String.valueOf(map.get("result_code")));
        //根据 out_trade_no 获取团购订单
        TuanOrders tuanOrders= tuanOrdersService.findByOut_trade_no(payBackDeal.getOut_trade_no());
        payBackDeal.setSaleId(tuanOrders.getSaleId());
        tPayBackDealService.save(payBackDeal);
    }

}
