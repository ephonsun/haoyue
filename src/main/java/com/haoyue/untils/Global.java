package com.haoyue.untils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by LiJia on 2017/8/23.
 * 封装了全局会用到的数据
 */
public class Global {

    public static String message_tolong="内容过长";
    public static String first_login="首次登陆，初始数据为0";
    public static String phone_isnull="该手机号未注册";
    public static String pay_type="微信支付";
    public static String invoice_type="不开发票";
    public static String order_code_begin="6688";//订单号开头
    public static Integer pageNumber=0;//默认第一页
    public static String alery_done="不可重复操作";
    public static List<String> excel_urls=new ArrayList<>();//存放excel存储路径，每天定时清空oss上的excel文件

    //基本返回数据
    public static  String do_success="操作成功";
    public static  String do_fail="操作失败";
    public static  String server_busying="服务器正忙，请稍后再试";
    public static  String openId_undefined="openId数据不正常";
    public static  String data_unexist="数据不存在";
    public static  String data_unright="输入数据不正常";
    public static  String coupon_isopen="优惠券活动开启，请输入优惠券进入店铺";
    public static  String coupon_isuse="该优惠已经被使用";
    public static  String coupon_not_exist="该优惠券不存在，请确认优惠码";

    //error code
    public static String user_unlogin="当前用户未登录"; // errorcode 101
    public static String openId_isNull="openId为空";  //102
    public static String ip_unright="ip数据不正常";//103

    //订单
    public static String order_unpay="待付款订单";
    public static String order_unsend="待发货订单";
    public static String order_send="待收货订单";
    public static String order_back="退货订单";
    public static String order_finsh="已完成订单";
    public static String amount_notEnough="库存量不足";
    public static String order_not_unpay="当前订单不是待付款订单";
    public static int count=1;
    public static boolean flag=false;
    public static Object object=new Object();//线程锁

    //物流模板
    public static String account_unright="请确保所有件数、体积、重量数格式正确";
    public static String price_unright="请确保所有初始邮费价格式正确";
    public static String more_account_unright="请确保所有续件数、续件体积、续件重量数格式正确";
    public static String more_price_unright="请确保所有续费邮费价格式正确";
    public static String template_exist="已经存在相同物流名的模板";

    //商品添加
    public static String discount_price_unright="折扣价输入不正确";
    public static String price_is_unright="原价输入不正确";
    public static String price_ls_discountprice="折扣价不应该大于原价";

    //文件上传
    public static String memoryspace_lack="您的个人存储空间不足";
    public static String file_toolarge="文件过大，请上传小于100M的文件";
    public static Integer max_file_size=1048576;// 1G=1048576kb

    //用户信息
    public static String user_isnull="用户不存在";
    public static String record_exist="信息已存在";
    public static String record_unexist="信息不存在";
    public static String have_no_right="无权操作";
    public static String service_stop="该店铺服务已到期或被强行停止";
    public static String seller_online="当前账号已被登录，你已被迫下线，如遇密码泄露，请尽快修改";

    //密码校验
    public static String oldPass_unRigt="原密码不正确";
    public static String oldPass_Rigt="原密码正确";

    //七牛云
    public static  String qiniu_href="http://ov2vxgobr.bkt.clouddn.com/";
    public static  String ACCESS_KEY = "WT0oOeMElaQP2DvY2-b3Y_zkqqpBz84x6pmnZexE";
    public static  String SECRET_KEY = "e1mi3tlQb8tcX9CCUvZlq-mpZmAQvjximYMVue7a";
    public static  String bucketname="haoyue";

    //阿里云
    //http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/10/10/1507615986025.jpg
    //http://cslapp.com/hymarket/2017/10/10/1507615986025.jpg
    public static String aliyun_href="http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/";
    public static String accessKeyId="LTAIRHJkjEByQvQL";
    public static String accessKeySecret = "pXTOa9qMzha20WKKTzl6DNDGQglNpB";
    public static String signName="苏州皓月科技";
    public static String TemplateCode="SMS_91140162";

    //微信支付
    public static String notify_url="https://www.cslapp.com/pay/notify";//通知地址
    public static String common_pay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单url

    //非拦截地址
    public static List<String> urls(){
        List<String> list=new ArrayList<>();
        list.add("/seller/login");
        list.add("/seller/reg");
        list.add("/seller/findOne");
        list.add("/seller/getPhoneCode");
        list.add("/seller/changePass");
        list.add("/shopCar/del");
        list.add("/order/del");
        list.add("/seller/pro/uploadPic");
        list.add("/seller/update");
        list.add("/customer/getSessionKey");
        list.add("/pay/notify");
        list.add("/websocket");
        list.add("/helloWord");
        list.add("/welcome");
        list.add("/leave-message");
        return list;
    }

}
