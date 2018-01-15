package com.haoyue.untils;

import org.apache.tomcat.util.codec.binary.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;

/**
 * Created by LiJia on 2017/9/7.
 * 解密微信小程序返回的数据
 */

public class WXAppletUserInfo {
    /**
     * 解密用户敏感数据
     */
    public String decodeUserInfo(String encryptedData,String iv,String session_key){

        try {
            byte[] resultByte = AESUtil.instance.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(session_key), Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                String userInfo = new String(resultByte, "UTF-8");
                System.out.println(userInfo);
                return userInfo;
                //JSONObject json = JSONObject.fromObject(userInfo); //将字符串{“id”：1}
                //renderJson(json);
                //System.out.println();
            }
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "null";
    }
}

