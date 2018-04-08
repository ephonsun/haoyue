package com.haoyue.untils;

import java.util.*;

/**
 * Created by LiJia on 2017/8/23.
 * 封装了全局会用到的数据
 */
public class Global {

    public static String code="199";
    public static String message_tolong="内容过长";
    public static String first_login="首次登陆，初始数据为0";
    public static String phone_isnull="该手机号未注册";
    public static String pay_type="微信支付";
    public static String invoice_type="不开发票";
    public static String order_code_begin="6688";//订单号开头
    public static Integer pageNumber=0;//默认第一页
    public static String alery_done="不可重复操作";
    public static List<String> excel_urls=new ArrayList<>();//存放excel存储路径，每天定时清空oss上的excel文件
    public static Object object3=new Object();//执行dictionarys表更新
    public static Map<String,String> access_tokens=new HashMap<>();//缓存 access_token

    //注册
    public static String appid_exist="appid已经存在";
    public static String authority_not_digit="权限值应填写数字";
    public static String appid_authority_isnull="appid和权限值不应为空";

    //会员
    public static String member_exist="当前会员信息已经存在";
    public static String discount_not_set="店主未设置会员折扣";
    public static String leavel_undefined="leavel值为undefined，不合法";
    public static String birthday_illegal="生日输入公式不合法,参照(年-月-日)";
    public static String member_froms_success="交易成功";
    public static String member_froms_fail="交易未成功";

    //抵用券
    public static String no_cashticket="抵用券已被抢完";
    public static String get_ticket_alerady="每人只能抢一张";

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
    public static  String pagenumber_not_right="当前页数不正确";

    //error code
    public static String user_unlogin="当前用户未登录"; // errorcode 101
    public static String openId_isNull="openId为空";  //102
    public static String ip_unright="ip数据不正常";//103

    //订单
    public static String order_unpay="待付款订单";
    public static String order_luckdraw_unpay="待付款抽奖订单";
    public static String order_luckdraw="抽奖订单";
    public static String order_unsend="待发货订单";
    public static String order_send="待收货订单";
    public static String order_back="退货订单";
    public static String order_finsh="已完成订单";
    public static String amount_notEnough="库存量不足";
    public static String order_not_unpay="当前订单不是待付款订单";
    public static String luckdraw_num_enough="抽奖人数已满";
    public static String luckdraw_end_ornotbegin="抽奖活动已结束或未开启";
    public static String access_in_again="同一个用户不可重复抽奖";
    public static String cannot_get_info="请设置允许获取信息后再提交";
    public static int count=1;
    public static boolean flag=false;
    public static Object object=new Object();//线程锁
    public static Object object2=new Object();

    //物流模板
    public static String account_unright="请确保所有件数、体积、重量数格式正确";
    public static String price_unright="请确保所有初始邮费价格式正确";
    public static String more_account_unright="请确保所有续件数、续件体积、续件重量数格式正确";
    public static String more_price_unright="请确保所有续费邮费价格式正确";
    public static String template_exist="已经存在相同物流名的模板";
    public static String weight_required="顺丰快递的计价方式须为重量计费";

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
    public static String customer_not_only="用户信息重复";
    public static String wxname_isNullorBlank="无法获取微信名称";

    //密码校验
    public static String oldPass_unRigt="原密码不正确";
    public static String oldPass_Rigt="原密码正确";

    //七牛云
    public static  String qiniu_href="http://ov2vxgobr.bkt.clouddn.com/";
    public static  String ACCESS_KEY = "WT0oOeMElaQP2DvY2-b3Y_zkqqpBz84x6pmnZexE";
    public static  String SECRET_KEY = "e1mi3tlQb8tcX9CCUvZlq-mpZmAQvjximYMVue7a";
    public static  String bucketname="haoyue";

    //阿里云
    //原始访问地址 http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/10/10/1507615986025.jpg
    //替换成皓月域名访问 http://cslapp.com/hymarket/2017/10/10/1507615986025.jpg
    // 二维码 http://cslapp.com/hymarket/qrcodes/149.jpg
    public static String aliyun_href="http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/";
    public static String accessKeyId="LTAIRHJkjEByQvQL";
    public static String accessKeySecret = "pXTOa9qMzha20WKKTzl6DNDGQglNpB";
    public static String signName="苏州皓月科技";
    public static String TemplateCode="SMS_91140162";
    public static String TemplateCode2="SMS_115765225";
    public static String TemplateCode4="SMS_123796676";

    //微信支付
    public static String notify_url="https://www.cslapp.com/pay/notify";//通知地址
    public static String common_pay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单url

    // 付款package
    public static Map<String,String> package_map=new HashMap<>();

    //评价
    public static Object object4=new Object();
    public static List<String> sendsms3=new ArrayList<>();//存放退款通知的卖家ID
    public static boolean timer=false;

    //签到
    public static String already_signin="不可以重复签到";
    public static String activity_closed="活动已经关闭";

    //微信模板信息访问通知
    public static String wxtemplate_msg1="3月5日各种活动：小香套装最低360元起，还有各种团购，秒杀，记得围观哦！";//日常访问通知
    public static String wxtemplate_msg2="亲爱的，您喜欢的宝贝上架啦，貌似新品有限时折扣！";//预售通知
    public static String wxtemplate_msg3="置顶关注，限时巨划算怎能错过！";//秒杀通知
    public static Map<String,String> yushou_map=new HashMap<>();
    public static Map<String,String> miaosha_map=new HashMap<>();


    //申请退款
    public static String already_apply_payback="不可多次申请退款/退货";
    public static String already_cancel="当前订单不可重复撤销";
    public static String apply_order_date_expire="当前订单已超过申请退款/货有效期";
    public static String not_can_apply_cancel_true="已撤销订单不可操作";

    //快递
    public static String kuaidi_key="d348100a2fd46f0dc1aeddde3f0a904c";
    public static String not_receive="当前快递未签收";

    //积攒送小程序
    public static int thumbs_up_num=40;//积攒目标数
    public static String thumbs_up_access="集赞数已达到啦！";
    public static String already_apply_thumb_up="你已经申请过积攒啦！";
    public static String already_help_thumb_up="不可重复点赞";
    public static String already_thumb_up_expire="当前用户申请的积攒活动已经失效";
    public static String can_not_thumb_up_self="不可为自己点赞";

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
