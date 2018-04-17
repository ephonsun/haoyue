package com.haoyue.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haoyue.pojo.Seller;
import com.haoyue.service.SellerService;
import com.haoyue.untils.*;
import com.haoyue.wxpay.UUIDHexGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weixin4j.WeixinException;
import org.weixin4j.http.HttpsClient;
import org.weixin4j.http.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijia on 2018/4/16.
 * 微信卡券(暂存)
 */
@RequestMapping("/wxcard")
public class WxCardController {

    @Autowired
    private SellerService sellerService;

    public static ApiTicket ticket = null;

    @RequestMapping("/getsignin")
    public Result getCardSign(String card_id, String openId,String sellerId) throws WeixinException, JsonParseException, JsonMappingException, IOException {
        Map<String, String> ret = new HashMap<String, String>();
        //先要获取api_ticket,由于请求api_ticket的接口访问有次数限制，所以最好将获得到的api_ticket保存到缓存中，这边做法比较简单，直接使用的静态变量
        if (ticket == null || ticket.getExpires_in() < System.currentTimeMillis()) {
            //创建请求对象
            HttpsClient http = new HttpsClient();
            ObjectMapper mapper = new ObjectMapper();
            //这里获取的token就是最上方代码保存的微信公众号全局静态变量token
            String token = getToken(sellerId);
            //通过access_token调用获取api_ticket接口
            Response res = http.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=wx_card");
            System.out.println(res.asString());
            ticket = mapper.readValue(res.asString(), ApiTicket.class);
        }

        String api_ticket = ticket.getTicket();
        //注意用System.currentTimeMillis()获取的是毫秒，加密要求的是秒，要除以1000才可以
        String timestamp = (System.currentTimeMillis()/1000) + "";
        String nonce_str = UUIDHexGenerator.generate();
        WeixinSignature weixinSignature = new WeixinSignature(api_ticket, timestamp, card_id, nonce_str);
        //生成领取卡券需要的签名，并返回相关的参数
        String sign = weixinSignature.sign();
        SigninResult signinResult = new SigninResult(nonce_str, openId, timestamp, sign);
        return new Result(false, Global.do_success, signinResult, null);
    }

    public String getToken(String sellerId) {
        Seller seller = sellerService.findOne(Integer.parseInt(sellerId));
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=" + seller.getAppId() + "&secret=" + seller.getSecret();
        String access_token = HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        return access_token;
    }


    class SigninResult {
        private String nonce_str;
        private String openId;
        private String timestamp;
        private String signature;

        public SigninResult(String nonce_str, String openId, String timestamp, String signature) {
            this.nonce_str = nonce_str;
            this.openId = openId;
            this.timestamp = timestamp;
            this.signature = signature;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

}
