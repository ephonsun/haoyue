package com.haoyue.untils;

/**
 * Created by LiJia on 2017/8/22.
 */

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 七牛上传
 */

public class QiNiuUpload {

    private static final String ACCESS_KEY = Global.ACCESS_KEY;
    private static final String SECRET_KEY = Global.SECRET_KEY;

    //要上传的空间名
    private static final String bucketname = Global.bucketname;


    //简单上传

    /**
     * 初始化一个上传管理对象(七牛提供)
     *
     * @return
     */
    private UploadManager getUploadManager() {
//        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        return new UploadManager(c);
    }

    /**
     * 获得一个上传至七牛的token值
     *
     * @param postfix
     * @return
     */
    public String getUpToken(String postfix) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        //密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //简单上传，使用默认策略，只需要设置上传的空间名就可以了
       StringMap x = new StringMap();
       String fileName=year+"/"+(month+1)+"/"+ day+"/"+cal.getTimeInMillis()+postfix;
       x.put("saveKey", fileName);
        return auth.uploadToken(bucketname, null, 3600, x);
    }

    /**
     * 调用方法传入参数开始上传,返回成功后的文件保存路径
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public String upload(byte[] file, String fileName) throws IOException {	//不仅仅是byte数组,也可以是文件地址,文件,
        try {
            String postfix="";
            if (fileName.contains(".")) {
               postfix= fileName.substring(fileName.lastIndexOf("."), fileName.length());//获得文件名的后缀
            }
            else {
                postfix=fileName;
            }
            //调用put方法上传
            Response res = getUploadManager().put(file, null, getUpToken(postfix));
            //打印返回的信息
            String resBodyStr = res.bodyString();
             Map<String, String> map = JacksonUtil.json2Bean(resBodyStr, HashMap.class);
            //上传成功后返回的保存文件的名字
           //System.out.println("----------"+map.get("key"));
            return map.get("key");
        } catch (QiniuException e) {
            Response r = e.response;
            return r.bodyString();
        }
    }

}