package com.haoyue.untils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiJia on 2017/12/18.
 * 微信提供的商品生成二维码
 */
public class QRcode {

    public static String getminiqrQr(String accessToken,String pid) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String filename="";
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token="+accessToken;
            Map<String,Object> param = new HashMap<>();
            param.put("path", "pages/details/details?id="+pid);
            param.put("width", 430);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);
            //获取项目根路径
            String relativelyPath = System.getProperty("user.dir");
            String mkdirs=relativelyPath+"/erweima/";
            filename=relativelyPath+"/erweima/"+pid+".jpg";
            File filedirs = new File(mkdirs);
            File file=new File(filename);
            if (!filedirs.isDirectory()){
                filedirs.mkdirs();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filename;
    }
}

