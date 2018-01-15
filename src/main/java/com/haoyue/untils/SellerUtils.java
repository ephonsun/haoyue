package com.haoyue.untils;


import com.haoyue.pojo.Seller;

/**
 * Created by LiJia on 2017/8/21.
 * 卖家工具类
 */
public class SellerUtils {
    public static Seller hidePass(Seller old){
        old.setSellerPass("******");
        return old;
    }
}
