package com.haoyue;

import com.haoyue.tuangou.wxpay.HttpRequest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiJia on 2017/10/16.
 */

public class MyTest {

    public static Map<String, String> map = new HashMap<>();

    public static void main(String[] args) {

//        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
//        String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
//        String access_token = HttpRequest.sendPost(access_token_url, param1);
//        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
//        System.out.println(access_token);
        // 5_Gt2PwT1ir_BM3MPRocYfE0o74D_fQjvaW_LIqH1KFoDZJzEenefThH9MtDcUzYLqlZwdZ9p137rMv18O9PNMVe9yrWXrTZ060WMXzgk3izs_kJyXV5rfJ3x-ayxvM66efUhf52ZAYw6tuFXJRYDeAGAVCH
        //5_9VNbv_zq3EmtM4uJocYfE0o74D_fQjvaW_LIqH1KFoDZJzEenefThH9MtDcvf7wy0AtkF7A99dF1sD3GjxjH2MscV0mcU-sV8QszeAFYG2V71KfRMJKSmjBu0tauKI4jQuiuLpdamNIwRGDoDRFeAGAIWK
        map.put("1","1");
        map.put("2","2");
        System.out.println(map.get("1"));
        map.clear();
        System.out.println(map.get("1"));
        map.put("1","1");
        map.put("2","2");
        System.out.println(map.get("1"));
    }
}




