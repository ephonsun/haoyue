package com.haoyue.untils;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by Lijia on 2018/4/16.
 * 主要负责对微信卡券进行加密
 */
public class WeixinSignature {

    private  String api_ticket;
    private  String timestamp;
    private  String card_id;
    private  String nonce_str;

    public WeixinSignature(String api_ticket, String timestamp, String card_id, String nonce_str){
        this.api_ticket = api_ticket;
        this.timestamp = timestamp;
        this.card_id = card_id;
        this.nonce_str = nonce_str;
    }

    /**
     * 加密参数
     * @return
     */
    public String sign(){

        String[] str = {api_ticket,timestamp,card_id,nonce_str};
        Arrays.sort(str);
        String string = new String();
        for(int i=0; i<4; i++){
            string += str[i];
        }
        String signature = null;    //加密结果
        signature =getSha1(string);  //Sha1加密
        return signature;
    }


    //Sha1加密
    public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
