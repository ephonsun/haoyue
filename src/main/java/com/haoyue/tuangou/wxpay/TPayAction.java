package com.haoyue.tuangou.wxpay;

/**
 * Created by LiJia on 2017/9/12.
 */


import com.haoyue.tuangou.Exception.TMyException;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.pojo.TuanOrders;
import com.haoyue.tuangou.service.TOrdersService;
import com.haoyue.tuangou.service.TuanOrdersService;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/tuan/pay")
public class TPayAction {

    @Autowired
    private TPayDealService TPayDealService;
    @Autowired
    private TuanOrdersService tuanOrdersService;
    @Autowired
    private TOrdersService tOrdersService;

    /**
     * 小程序端请求的后台action，后台调用统一下单URL，对返回数据再次签名后，把数据传到前台
     * 前后再调用 wx.request(object) 进行支付
     */
    @RequestMapping("/do")
    public JSONArray pay(HttpServletRequest request,String body, String ordercode, String appId, String mchId, String ip, String openId, String key1, String session_key, String total_fee) throws UnsupportedEncodingException, DocumentException, TMyException {
        synchronized (TGlobal.pay_object) {
            if (StringUtils.isNullOrBlank(openId)) {
                throw new TMyException(TGlobal.openId_isNull, null, 102);
            }
            //body = new String(body.getBytes("UTF-8"), "ISO-8859-1");
            String appid = "替换为自己的小程序ID";//小程序ID
            appid = appId;
            String mch_id = "替换为自己的商户号";//商户号
            mch_id = mchId;
            ip=getIpAddr(request);
            String nonce_str = UUIDHexGenerator.generate();//随机字符串
            String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String code = PayUtil.createCode(8);
            String out_trade_no = ordercode;//商户订单号=订单号
            // 更新团购订单的商户订单号
            if (ordercode.startsWith("666")) {
                updateOrder(ordercode, out_trade_no);
            }
            // 更新普通订单的商户订单号
            if (ordercode.startsWith("888")) {
                updateOrder2(ordercode, out_trade_no);
            }
            String spbill_create_ip = "替换为自己的终端IP";//终端IP
            spbill_create_ip = ip;
            String notify_url = TGlobal.notify_url;//通知地址
            String trade_type = "JSAPI";//交易类型
            String openid = "替换为用户的openid";//用户标识
            openid = openId;
        /**/
            PaymentPo paymentPo = new PaymentPo();
            paymentPo.setAppid(appid);
            paymentPo.setMch_id(mch_id);
            paymentPo.setNonce_str(nonce_str);
            //String newbody = new String(body.getBytes("ISO-8859-1"), "UTF-8");//以utf-8编码放入paymentPo，微信支付要求字符编码统一采用UTF-8字符编码
            paymentPo.setBody(body);
            paymentPo.setOut_trade_no(out_trade_no);
            paymentPo.setTotal_fee(total_fee);
            paymentPo.setSpbill_create_ip(spbill_create_ip);
            paymentPo.setNotify_url(notify_url);
            paymentPo.setTrade_type(trade_type);
            paymentPo.setOpenid(openid);
            // 把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap();
            sParaTemp.put("appid", paymentPo.getAppid());
            sParaTemp.put("mch_id", paymentPo.getMch_id());
            sParaTemp.put("nonce_str", paymentPo.getNonce_str());
            sParaTemp.put("body", paymentPo.getBody());
            sParaTemp.put("out_trade_no", paymentPo.getOut_trade_no());
            sParaTemp.put("total_fee", paymentPo.getTotal_fee());
            sParaTemp.put("spbill_create_ip", paymentPo.getSpbill_create_ip());
            sParaTemp.put("notify_url", paymentPo.getNotify_url());
            sParaTemp.put("trade_type", paymentPo.getTrade_type());
            sParaTemp.put("openid", paymentPo.getOpenid());
            // 除去数组中的空值和签名参数
            Map<String, String> sPara = PayUtil.paraFilter(sParaTemp);
            String prestr = PayUtil.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            //key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
            String key = "&key=替换为商户支付密钥"; // 商户支付密钥
            key = "&key=" + key1;
            //MD5运算生成签名
            String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();


            paymentPo.setSign(mysign);
            //打包要发送的xml
            String respXml = MessageUtil.messageToXML(paymentPo);
            // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”
            respXml = respXml.replace("__", "_");
            String url = TGlobal.common_pay_url;//统一下单API接口链接
            String param = respXml;
            //String result = SendRequestForUrl.sendRequest(url, param);//发起请求
            String result = PayUtil.httpRequest(url, "POST", param);
            // 将解析结果存储在HashMap中
            Map map = new HashMap();
            InputStream in = new ByteArrayInputStream(result.getBytes());
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            @SuppressWarnings("unchecked")
            List<Element> elementList = root.elements();
            for (Element element : elementList) {
                map.put(element.getName(), element.getText());
            }
            // 返回信息
            String return_code = (String) map.get("return_code");//返回状态码
            String return_msg = (String) map.get("return_msg");//返回信息
            System.out.println("return_msg" + return_msg);
            System.out.println("return_code" + return_code);
            JSONObject JsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                // 业务结果
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                String nonceStr = UUIDHexGenerator.generate();
                JsonObject.put("nonceStr", nonceStr);
                JsonObject.put("package", "prepay_id=" + prepay_id);
                TGlobal.tuan_package_map.put(openId, prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                JsonObject.put("timeStamp", timeStamp + "");
                String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名
                //String paySign = PayUtil.sign(stringSignTemp, "&key=替换为自己的密钥", "utf-8").toUpperCase();
                String paySign = PayUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();
                JsonObject.put("paySign", paySign);
                jsonArray.add(JsonObject);
            }
            return jsonArray;
        }
    }

    @RequestMapping("/uuid")
    public String uuid() {
        String nonceStr = UUIDHexGenerator.generate();
        return nonceStr;
    }

    public  String getIpAddr(HttpServletRequest request) {
        String ip  =  request.getHeader( " x-forwarded-for " );
        if  (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip)) {
            ip  =  request.getHeader( " Proxy-Client-IP " );
        }
        if  (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip)) {
            ip  =  request.getHeader( " WL-Proxy-Client-IP " );
        }
        if  (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip)) {
            ip  =  request.getRemoteAddr();
        }
        return  ip;
    }

    /**
     * 微信支付结果通知
     */
    @RequestMapping("/notify")
    public void notifya(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);
        Map<String, String> map = PayUtil.doXMLParse(notityXml);
        for (String key : map.keySet()) {
            System.out.println(key + "==" + map.get(key));
        }
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            System.out.println("success");
            //验证签名是否正确
//            if(PayUtil.verify(PayUtil.createLinkString(map), (String)map.get("sign"), WxPayConfig.key, "utf-8")){
//                /**此处添加自己的业务逻辑代码start**/
//                /**此处添加自己的业务逻辑代码end**/
//                //通知微信服务器已经支付成功
//                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//            }
//        }else{
//            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//        }
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            System.out.println("fail");
        }

        //将每次支付结果通知信息保存到数据库
        TPayDeal TPayDeal = new TPayDeal();
        TPayDeal.setOpenId(map.get("openid"));
        TPayDeal.setMch_id(map.get("mch_id"));
        TPayDeal.setReturn_code(map.get("return_code"));
        TPayDeal.setTotal_fee(map.get("total_fee"));
        TPayDeal.setTransaction_id(map.get("transaction_id"));
        TPayDeal.setDate(StringUtils.formatDate(map.get("time_end")));
        TPayDeal.setAppId(map.get("appid"));
        TPayDeal.setOut_trade_no(map.get("out_trade_no"));
        TPayDealService.save(TPayDeal);

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }


    /**
     * 支付结果查询
     * 接口留下备用
     *
     * @param appId
     * @param machId
     * @param transaction_id 微信支付订单号 transaction_id  从notify通知处 获得
     * @param key
     */
    @RequestMapping("/check")
    public void check(String appId, String machId, String transaction_id, String key) {
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        String nonceStr = UUIDHexGenerator.generate();
        Map map = new HashMap();
        map.put("appid", appId);
        map.put("mch_id", machId);
        map.put("transaction_id", transaction_id);
        map.put("nonce_str", nonceStr);
        String prestr = PayUtil.createLinkString(map);
        String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();
        String param = "appid=" + appId + "&mch_id=" + machId + "&transaction_id=" + transaction_id + "&nonce_str=" + nonceStr + "&sign=" + mysign;
    }

    public void updateOrder(String ordercode, String out_trade_no) {
        TuanOrders tuanOrders = tuanOrdersService.findByCode(ordercode);
        tuanOrders.setOut_trade_no(out_trade_no);
        tuanOrdersService.update(tuanOrders);
    }

    public void updateOrder2(String ordercode, String out_trade_no) {
        TOrders tOrders = tOrdersService.findByCode(ordercode);
        tOrders.setOut_trade_no(out_trade_no);
        tOrdersService.update(tOrders);
    }


}
