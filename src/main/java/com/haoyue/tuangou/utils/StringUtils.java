package com.haoyue.tuangou.utils;

import com.haoyue.untils.HanyuPinyinHelper;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符工具类
 */

public class StringUtils {

    public static String fromCharset(String str, String charset) {
        try {
            return new String(str.getBytes(charset), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getYMD(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String str= simpleDateFormat.format(date);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNullOrBlank(String param) {
        return ((null == param) || ("".equalsIgnoreCase(param.trim())) || ("null".equalsIgnoreCase(param.trim())));
    }


    public static String  checkCode(Integer amount){
        String str="0123456789ZXCVBNMASDFGHJKLQWERTYUIOPzxcvbnmlkjhgfdsapoiuytrewq";
        int index=0;
        String code="";
        for(int i=0;i<amount;i++){
            index=(int)(Math.random()*str.length());
            code+=str.charAt(index);
        }
         return code;
    }

    public static String getPhoneCode(){
        String str="0123456789";
        int index=0;
        String code="";
        for(int i=0;i<4;i++){
            index=(int)(Math.random()*str.length());
            code+=str.charAt(index);
        }
        return code;
    }

    public static Date formatDate(String time_end) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");//20170919162825
        Date nomat= sdf.parse(time_end);
        return nomat;//2017-9-19 16:28:25
    }

    public static Date formatStrToDate(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(str);
    }

    public static Date formatStrToDate2(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.parse(str);
    }

    public static String formDateToStr(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    //当前日期的一年后日期
    public static  Date addOneYear(){
        Date date=new Date();
        date.setYear(date.getYear()+1);
        return date;
    }

    public static  String getstrDate(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);

    }
    //当前日期的二十年后日期
    public static  Date addAllYear(){
        Date date=new Date();
        date.setYear(date.getYear()+20);
        return date;
    }

    public  static  boolean isDiget(String str) {
        if (StringUtils.isNullOrBlank(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).equals(".")){
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getPinYinByStr(String str){
        char[] chars=str.toCharArray();
        String result="";
        for(int i=0;i<chars.length;i++){
            if (String.valueOf(chars[i]).equals(" ")){
                continue;
            }
            if(Character.isDigit(chars[i])){
                result+=chars[i];
                continue;
            }
            if(chars[i] >= 0x0391 && chars[i]<= 0xFFE5) //中文字符
            {
                result+= HanyuPinyinHelper.getFirstLetter(chars[i]+"");
            }
            if(chars[i] >=0x0000 && chars[i] <=0x00FF){ //英文字符
                result+=chars[i];
            }
        }
        return result;
    }
}

