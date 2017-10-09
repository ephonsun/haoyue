package com.haoyue.untils;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiJia on 2017/9/15.
 * 快递名
 */
public class kuaidiname {

    public static String getName(String key) {
        Map<String, String> map = new HashMap<>();
        map.put("百世汇通", "huitongkuaidi");
        map.put("申通", "shentong");
        map.put("顺丰", "shunfeng");
        map.put("天天快递", "tiantian");
        map.put("圆通速递", "yuantong");
        map.put("韵达快运", "yunda");
        map.put("中通速递", "zhongtong");
        map.put("EMS", "ems");
        return map.get(key);
    }
}
