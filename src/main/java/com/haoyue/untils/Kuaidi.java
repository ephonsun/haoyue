package com.haoyue.untils;

/**
 * Created by Administrator on 2018/1/29.
 */
public class Kuaidi {

    public static boolean getStatus(String code) {

        //快递查询 京东万象  https://wx.jcloud.com/market/datas/26/10307
        String url = "https://way.jd.com/jisuapi/query";
        String param = "type=auto&number=" + code + "&appkey=" + Global.kuaidi_key;
        String result = HttpRequest.sendGet(url, param);
        System.out.println(result);
        // true 签收 false 未签收
        boolean flag = false;
        try {
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
            System.out.println(jsonObject.getJSONObject("result"));
            if (jsonObject.getJSONObject("result").getString("status").equals("0")) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


}
