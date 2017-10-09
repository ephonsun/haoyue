package com.haoyue.untils;

/**
 * Created by LiJia on 2017/9/15.
 * 快递查询
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class kuaidi100 {

    /**
     * @param id    身份授权key
     * @param com   要查询的快递公司代码
     * @param nu    要查询的快递单号
     * @param show  返回类型： 0：返回json字符串，
     * @param muti  返回信息数量： 1:返回多行完整的信息，
     * @param order 排序： desc：按时间由新到旧排列，  asc：按时间由旧到新排列。
     */
    public static void getMessage(String id, String com, String nu, String show, String muti, String order) {
        try {
            URL url = new URL("http://api.kuaidi100.com/api?id=" + id + "&com=" + com + "&nu=" + nu + "&show=" + show + "&muti=" + muti + "&order=" + order);
            URLConnection con = url.openConnection();
            con.setAllowUserInteraction(false);
            InputStream urlStream = url.openStream();
            String type = con.guessContentTypeFromStream(urlStream);
            String charSet = null;
            if (type == null)
                type = con.getContentType();

            if (type == null || type.trim().length() == 0 || type.trim().indexOf("text/html") < 0)
                return;

            if (type.indexOf("charset=") > 0)
                charSet = type.substring(type.indexOf("charset=") + 8);

            byte b[] = new byte[10000];
            int numRead = urlStream.read(b);
            String content = new String(b, 0, numRead);
            while (numRead != -1) {
                numRead = urlStream.read(b);
                if (numRead != -1) {
                    //String newContent = new String(b, 0, numRead);
                    String newContent = new String(b, 0, numRead, charSet);
                    content += newContent;
                }
            }
            //System.out.println("content:" + content);
            urlStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

