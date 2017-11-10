//package com.haoyue.wxpayback;
//
///**
// * Created by LiJia on 2017/11/2.
// */
//
//import com.yupaopao.trade.api.account.response.WXGzhResponse;
//import com.yupaopao.trade.api.account.response.WXPayResponse;
//import com.yupaopao.trade.api.gateway.code.*;
//import com.yupaopao.trade.api.gateway.request.PayRequest;
//import com.yupaopao.trade.api.gateway.request.RefundRequest;
//import com.yupaopao.trade.api.gateway.request.WeixinOrderFindRequest;
//import com.yupaopao.trade.api.gateway.sdk.qqpay.config.ClientCustomSSL;
//import com.yupaopao.trade.api.gateway.utils.WxUtil;
//import com.yupaopao.trade.api.payment.code.PaymentConstant;
//import com.yupaopao.trade.api.payment.dto.YppException;
//import com.yupaopao.trade.api.utils.IdWorker;
//import com.yupaopao.trade.common.config.ThirdUrlConfig;
//import com.yupaopao.trade.common.exception.TradeException;
//import com.yupaopao.trade.domain.mapper.PayMapper;
//import com.yupaopao.trade.domain.mapper.PaymentDetailMapper;
//import com.yupaopao.trade.domain.mapper.RefundDetailMapper;
//import com.yupaopao.trade.domain.model.PaymentDetail;
//import com.yupaopao.trade.domain.model.RefundDetail;
//import com.yupaopao.trade.gateway.service.WxPayService;
//import com.yupaopao.trade.payment.service.TradeService;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ResourceUtils;
//
//import javax.net.ssl.SSLContext;
//import javax.servlet.http.HttpServletRequest;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.security.KeyStore;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
///**
// * Created by zhuliangxing on 2017/4/21.
// */
//@Service
//public class WxpayServiceImpl implements WxPayService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(WxpayServiceImpl.class);
//
//    @Autowired
//    private PayMapper payMapper;
//
//    @Autowired
//    private TradeService tradeService;
//
//    @Value("${third.callback.net_url}")
//    private String notifyUrl;
//
//    @Autowired
//    private PaymentDetailMapper paymentDetailsMapper;
//
//    @Autowired
//    private RefundDetailMapper refundDetailMapper;
//
//    /**
//     * 统一下单
//     *
////     * @param requesta
//     * @return
//     */
//    public WXPayResponse wxPay(PayRequest request) {
//        WXPayResponse response = new WXPayResponse();
//
//        PaymentDetail detail = null;
//        Map<String, String> data = null;
//        try {
//            // 组装XML
//            String xml = WXParamGenerate(request.getSubject(), request.getOutTradeNo(), request.getTotalFee());
//            // 发送http请求到微信服务端，获取返回的参数
//            String res = WxUtil.httpsRequest(WXPayConstants.WX_PAYURL, "POST", xml);
//            data = WxUtil.doXMLParse(res);
//            detail = new PaymentDetail();
//            detail.setCallbackStatus(CallbackType.unCall.getStatusValue());
//            detail.setUserId(request.getUserId());
//            detail.setType(PayType.wxPay.getStatusValue());
//            detail.setPaymentId(request.getPaymentId());
//            detail.setOrderType(request.getOrderType());
//            detail.setTotalFee(request.getTotalFee());
//            detail.setStatus(PayStatus.unPay.getStatusValue());
//            detail.setOutTradeNo(request.getOutTradeNo());
//            detail.setSubject(request.getSubject());
//
//            Map<String, String> param = new HashMap<String, String>();
//            param.put("appid", WXPayConstants.APP_ID);
//
//            param.put("partnerid", WXPayConstants.MCH_ID);
//            param.put("prepayid", data.get("prepay_id"));
//            param.put("noncestr", data.get("nonce_str"));
//
//            String timeStamp = System.currentTimeMillis() / 1000 + "";
//            param.put("timestamp", timeStamp);
//            param.put("package", "Sign=WXPay");
//            String sign = WxUtil.generateSignature(param, WXPayConstants.WX_PARTNERKEY, SignType.MD5);
//            response.setAppId(WXPayConstants.APP_ID);
//            response.setPartnerId(WXPayConstants.MCH_ID);
//            response.setTimestamp(timeStamp);
//            response.setNoncestr(data.get("nonce_str"));
//            response.setSign(sign);
//            response.setPrepayId(data.get("prepay_id"));
//            paymentDetailsMapper.insertPayment(detail);
//            return response;
//        } catch (Exception e) {
//            throw new YppException(PayException.xmlParseError);
//        }
//    }
//
//    /**
//     * 微信统一下单参数设置
//     *
//     * @param description
//     * @param orderOutId
//     * @param totalFee
//     * @return
//     * @throws Exception
//     */
//    public String WXParamGenerate(String description, String orderOutId, Long totalFee) throws Exception {
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("appid", WXPayConstants.APP_ID);
//        param.put("mch_id", WXPayConstants.MCH_ID);
//        param.put("nonce_str", WxUtil.generateNonceStr());
//        param.put("body", description);
//        param.put("out_trade_no", orderOutId);
//        param.put("total_fee", totalFee + "");
//        param.put("spbill_create_ip", WxUtil.GetIp());
//        param.put("notify_url", notifyUrl + WXPayConstants.NOTIFU_URL);
//        param.put("trade_type", "APP");
//        String sign = WxUtil.generateSignature(param, WXPayConstants.WX_PARTNERKEY, SignType.MD5);
//        param.put("sign", sign);
//        return WxUtil.GetMapToXML(param);
//    }
//
//    /**
//     * 拼接微信退款XML
//     *
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    public static SortedMap<String, String> WXRefundParamGenerate(RefundRequest request) {
//
//        SortedMap<String, String> param = new TreeMap<String, String>();
//        try {
//            param.put("appid", WXPayConstants.APP_ID);
//            param.put("mch_id", WXPayConstants.MCH_ID);
//            param.put("nonce_str", WxUtil.generateNonceStr());
//            param.put("out_trade_no", request.getOutTradeNo());
//            // 支付网关生成订单流水
//            param.put("out_refund_no", String.valueOf(IdWorker.nextId()));
//            param.put("total_fee", String.valueOf(request.getTotalFee()));// 单位为分
//            param.put("refund_fee", String.valueOf(request.getRefundFee()));// 单位为分
//            param.put("op_user_id", WXPayConstants.MCH_ID);// 操作人员,默认为商户账号
//            String sign = WxUtil.generateSignature(param, WXPayConstants.WX_PARTNERKEY, SignType.MD5);
//            param.put("sign", sign);
//        } catch (Exception e) {
//            throw new YppException(PayException.signError);
//        }
//        return param;
//    }
//
//    /**
//     * 解析微信返回的xml 参数
//     *
//     * @param request
//     */
//    public Map<String, String> xmlParserCallback(HttpServletRequest request) {
//        Map<String, String> map = null;
//        BufferedReader reader = null;
//        String line = "";
//        String xmlString = null;
//        try {
//            reader = request.getReader();
//            StringBuffer inputString = new StringBuffer();
//
//            while ((line = reader.readLine()) != null) {
//                inputString.append(line);
//            }
//            xmlString = inputString.toString();
//            request.getReader().close();
//            LOGGER.info("----接收到的数据如下：---" + xmlString);
//            map = WxUtil.doXMLParse(xmlString);
//        } catch (Exception e) {
//            throw new YppException(PayException.xmlParseError);
//        }
//        return map;
//    }
//
//    /**
//     * IO解析获取微信的数据
//     *
//     * @param request
//     * @return
//     */
//    public String getXmlString(HttpServletRequest request) {
//        BufferedReader reader = null;
//        String line = "";
//        String xmlString = null;
//        try {
//            reader = request.getReader();
//            StringBuffer inputString = new StringBuffer();
//
//            while ((line = reader.readLine()) != null) {
//                inputString.append(line);
//            }
//            xmlString = inputString.toString();
//        } catch (Exception e) {
//            throw new YppException(PayException.xmlParseError);
//        }
//
//        return xmlString;
//    }
//
//    /**
//     * 微信回调
//     *
//     * @param request
//     * @return
//     */
//    public String wxNotify(HttpServletRequest request) {
//        LOGGER.info("微信正在回调》》》》》》》》》");
//        PaymentDetail detail = null;
//        String xmlString = "";
//        String lastXml = "";
//
//        try {
//            xmlString = getXmlString(request);
//            LOGGER.info("微信返回的回调结果是：：：：：：：" + xmlString);
//            // 先解析返回的数据
//            Map<String, String> dataMap = WxUtil.xmlToMap(xmlString);
//            String returnCode = dataMap.get("return_code");
//            // 通信成功
//            if (ReturnStatus.success.getStatusValue().equals(returnCode)) {
//                LOGGER.info("通信成功++++++++++++");
//
//                // 验证通过才能记到流水表中，否则不计入
//                if (WxUtil.isSignatureValid(xmlString, WXPayConstants.WX_PARTNERKEY)) {
//
//                    try {
//                        detail = new PaymentDetail();
//                        detail.setTradeNo(dataMap.get("transaction_id"));
//                        detail.setOutTradeNo(dataMap.get("out_trade_no"));
//                        if (dataMap.get("result_code").equals(ReturnStatus.success)) {
//                            detail.setStatus(PayStatus.paySuccess.getStatusValue());
//                        } else {
//                            detail.setStatus(PayStatus.payFail.getStatusValue());
//                        }
//                        detail.setType(PayType.wxPay.getStatusValue());
//                        // 设置为已经回调
//                        detail.setCallbackStatus(CallbackType.callable.getStatusValue());
//                        paymentDetailsMapper.updatePayment(detail);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                tradeService.dealWithPayCallBack(dataMap.get("out_trade_no").toString(),
//                        dataMap.get("transaction_id").toString(), PaymentConstant.PayCallBackStatus.SUCCESS,
//                        PaymentConstant.TradeAction.WEIXIN_ACCOUNT);
//                lastXml = WxUtil.returnXML(ReturnStatus.success.getStatusValue());
//            } else {
//
//                lastXml = WxUtil.returnXML(ReturnStatus.fail.getStatusValue());
//            }
//        } catch (Exception e) {
//
//            throw new TradeException(GatewayCode.PayBackError);
//        }
//        LOGGER.info("最终给微信的结果是：" + lastXml);
//        return lastXml;
//
//    }
//
//    /**
//     * 微信退款
//     *
//     * @param request
//     * @return
//     */
//    public String wxRefund(RefundRequest request) {
//
//        RefundDetail detail = null;
//        // 拼装Map
//        SortedMap<String, String> map = WXRefundParamGenerate(request);
//        // 拼装XML
//        String requestXml = WxUtil.getRequestXml(map);
//        // 获取返回数据
//        String refundResult = "";
//        try {
//            refundResult = ClientCustomSSL.doRefund(WXPayConstants.WX_REFUND, requestXml);
//        } catch (Exception e) {
//            throw new TradeException(GatewayCode.RefundBackXmlError);
//        }
//        System.out.println("退款产生的json字符串：" + refundResult);
//        // 转换为Map
//        Map<String, String> responseMap = new HashMap<>();
//        try {
//            responseMap = WxUtil.doXMLParse(refundResult);
//        } catch (Exception e) {
//            throw new TradeException(GatewayCode.RefundBackXmlError);
//        }
//        String returnCode = responseMap.get("return_code");
//        // 通信正常
//        if (returnCode.equals(ReturnStatus.success.getStatusValue())) {
//            String resultCode = responseMap.get("result_code");
//            detail = new RefundDetail();
//            // 微信生成的退款ID
//            String tradeNo = responseMap.get("out_refund_no");
//
//            detail.setTradeNo(tradeNo);
//            detail.setRefundId(IdWorker.unique());
//            detail.setType(PayType.wxPay.getStatusValue());
//            detail.setUserId(request.getUserId());
//            detail.setSubject(responseMap.get("err_code_des"));
//            detail.setorderOutId(request.getOutTradeNo());
//            detail.setRefundFee(request.getRefundFee());
//            detail.setTotalFee(request.getTotalFee());
//            detail.setOriginTradeNo(tradeNo);
//            // 退款成功
//            if (resultCode.equals(ReturnStatus.success)) {
//                detail.setStatus(PayStatus.paySuccess.getStatusValue());
//                payMapper.saveRefund(detail);
//                return ReturnStatus.success.getStatusValue();
//
//            } else {
//                detail.setStatus(PayStatus.payFail.getStatusValue());
//                refundDetailMapper.insertRefund(detail);
//                return ReturnStatus.fail.getStatusValue();
//
//            }
//
//        } else {
//            throw new TradeException(GatewayCode.RefundHasGone);
//        }
//
//    }
//
//    /**
//     * 微信公众号支付下单
//     */
//    @Override
//    public WXGzhResponse wxGZHPay(PayRequest request) {
//        WXGzhResponse response = new WXGzhResponse();
//        PaymentDetail detail = null;
//        Map<String, String> data = null;
//        try {
//            // 组装XML
//            String xml = WXGZHParamGenerate(request.getSubject(), request.getOutTradeNo(), request.getTotalFee(),
//                    request.getPayExt().get("openId").toString());
//            // 发送http请求到微信服务端，获取返回的参数
//            String res = WxUtil.httpsRequest(WXPayConstants.WX_PAYURL, "POST", xml);
//            data = WxUtil.doXMLParse(res);
//            detail = new PaymentDetail();
//            detail.setCallbackStatus(CallbackType.unCall.getStatusValue());
//            detail.setUserId(request.getUserId());
//            detail.setType(PayType.wxPay.getStatusValue());
//            detail.setPaymentId(request.getPaymentId());
//            detail.setOrderType(request.getOrderType());
//            detail.setTotalFee(request.getTotalFee());
//            detail.setStatus(PayStatus.unPay.getStatusValue());
//            detail.setOutTradeNo(request.getOutTradeNo());
//            detail.setSubject(request.getSubject());
//
//            String timeStamp = System.currentTimeMillis() / 1000 + "";
//            Map<String, String> param = new HashMap<String, String>();
//            param.put("appId", WXPayConstants.WEIXIN_GZH_APPID);
//
//            param.put("signType", "MD5");
//            param.put("nonceStr", data.get("nonce_str"));
//
//            param.put("timeStamp", timeStamp);
//            param.put("package", "prepay_id=" + data.get("prepay_id"));
//
//            String sign = WxUtil.generateSignature(param, WXPayConstants.WEIXIN_GZH_KEY, SignType.MD5);
//
//            response.setAppId(WXPayConstants.WEIXIN_GZH_APPID);
//            response.setNonceStr(data.get("nonce_str"));
//            response.setPaySign(sign);
//            response.setTimeStamp(timeStamp);
//            response.setWxpackage("prepay_id=" + data.get("prepay_id"));
//            response.setSignType("MD5");
//            payMapper.saveDetail(detail);
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new YppException(PayException.xmlParseError);
//        }
//    }
//
//    /**
//     * 微信公众号统一下单
//     *
//     * @param description
//     * @param orderOutId
//     * @param totalFee
//     * @return
//     * @throws Exception
//     */
//    public String WXGZHParamGenerate(String description, String orderOutId, Long totalFee, String openId)
//            throws Exception {
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("appid", WXPayConstants.WEIXIN_GZH_APPID);
//        param.put("mch_id", WXPayConstants.WEIXIN_GZH_MACHID);
//        param.put("nonce_str", WxUtil.generateNonceStr());
//        param.put("body", description);
//        param.put("out_trade_no", orderOutId);
//        param.put("openid", openId);
//        param.put("total_fee", totalFee + "");
//        param.put("spbill_create_ip", WxUtil.GetIp());
//        param.put("notify_url", notifyUrl + WXPayConstants.NOTIFU_URL);
//        param.put("trade_type", "JSAPI");
//        String sign = WxUtil.generateSignature(param, WXPayConstants.WEIXIN_GZH_KEY, SignType.MD5);
//        param.put("sign", sign);
//        return WxUtil.GetMapToXML(param);
//    }
//
//    /**
//     * 微信公众号账单查询
//     */
//    @Override
//    public void wxGZHOrderFind(WeixinOrderFindRequest request) {
//        Map<String, String> data = null;
//        try {
//            String xml = WXGZHOrderGenerate(request.getOutTradeNo());
//            String res = WxUtil.httpsRequest(WXPayConstants.WEIXIN_ORDER_FIND_URL, "POST", xml);
//            data = WxUtil.doXMLParse(res);
//            // 通信异常
//            if (!data.get("return_code").equals("SUCCESS")) {
//                throw new TradeException(GatewayCode.CommunicationError);
//            }
//            if (!data.get("result_code").equals("SUCCESS")) {
//                throw new TradeException(GatewayCode.OrderFindErrror);
//            }
//            if (!data.get("trade_state").equals("SUCCESS")) {
//                throw new TradeException(GatewayCode.OrderPayError);
//            }
//
//        } catch (Exception e) {
//            throw new TradeException(GatewayCode.PayXmlError);
//        }
//
//    }
//
//    /**
//     * 微信公众号生成订单。
//     *
//     * @param orderOutId
//     * @return
//     * @throws Exception
//     */
//    public String WXGZHOrderGenerate(String orderOutId) throws Exception {
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("appid", WXPayConstants.WEIXIN_GZH_APPID);
//        param.put("mch_id", WXPayConstants.WEIXIN_GZH_MACHID);
//        param.put("nonce_str", WxUtil.generateNonceStr());
//        param.put("out_trade_no", orderOutId);
//        String sign = WxUtil.generateSignature(param, WXPayConstants.WEIXIN_GZH_KEY, SignType.MD5);
//        param.put("sign", sign);
//        return WxUtil.GetMapToXML(param);
//    }
//}
