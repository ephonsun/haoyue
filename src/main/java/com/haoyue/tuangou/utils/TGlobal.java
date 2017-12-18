package com.haoyue.tuangou.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiJia on 2017/11/2.
 */
public class TGlobal {


    public static String do_success="操作成功";
    public static String do_fail="操作失败";
    public static String have_no_right="无权操作";
    public static String deliver_template_exist="快递模板已经存在";
    public static String weight_required="顺丰快递的计价方式须为重量计费";
    public static Object object3=new Object();
    public static List<String> excel_urls=new ArrayList<>();//存放excel存储路径，每天定时清空oss上的excel文件
    public static  String server_busying="服务器正忙，请稍后再试";
    public static  String data_unright="传入数据不正常";
    public static boolean free_flag=false;
    public static Map<String,String> access_tokens=new HashMap<>();

    //分页
    public static String pagenumber_not_right="页数不正常";

    //注册 登录
    public static String tusersale_isnull="用户名或密码错误";
    public static String saleid_openid_isnull="saleId或者openId为空";
    public static String phone_notright="当前手机号不存在";

    //卖家更新信息
    public static String oldpass_not_right="原密码不正确";
    public static String oldpass_right="原密码正确";
    public static String out_line="当前账号已被登录，你已被迫下线，如遇密码泄露，请尽快修改";

    //阿里云
    //http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/10/10/1507615986025.jpg
    //http://cslapp.com/hymarket/2017/10/10/1507615986025.jpg
    public static String aliyun_href="http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/";
    public static String accessKeyId="LTAIRHJkjEByQvQL";
    public static String accessKeySecret = "pXTOa9qMzha20WKKTzl6DNDGQglNpB";
    public static String signName="苏州皓月科技";
    public static String TemplateCode="SMS_91140162";

    //上传文件
    public static String space_not_enough="存储空间不够";
    public static int max_FileSzie=1048576; // 1G
    public static Object object5=new Object();

    //商品上传
    public static String pro_isNull_blank="请确认颜色、尺码、价格、库存不为空";
    public static String pro_notdiget="请确认价格、库存为数字";
    public static String pro_name_null="商品名不能为空";
    public static String date_format_wrong="时间格式不正确,请参照'年-月-日 小时:分钟'";

    //订单
    public static String order_unpay="待付款订单";
    public static String order_unsend="待发货订单";
    public static String order_unreceive="待收货订单";
    public static String order_finsh="已完成订单";
    public static String ordercode_begin="888";
    public static String already_delay="你已确认过延迟";
    public static Object object=new Object();//普通订单保存锁
    public static String openid_isnull="请设置允许获取信息后再下单";
    public static String free_chance="恭喜你获得零元购的机会，点击前往查看！";

    //团购订单
    public static String tuan_order_unpay="待付款团购订单";
    public static String tuan_order_tuaning="正在拼团团购订单";
    public static String tuan_order_success="待发货团购订单";
    public static String tuan_order_unreive="待收货团购订单";
    public static String tuan_order_fail="拼团失败团购订单";
    public static String tuan_order_finsh="已完成团购订单";
    public static String tuan_ordercode_begin="666";
    public static String tuan_num_too_late="1001";
    public static String tuan_time_too_late="结束时间已到，你来晚了一步";
    public static String tuan_times_nums_illegal="当前商品开团人数或时间异常";
    public static String have_joined_in="你已参加当前团购！";
    public static String date_not_between_tuandate="当前时间不在团购时间内";
    public static String tuan_nums_not_enough="开团人数未达到";
    public static String tuan_comment="您的退款将在24小时内退回,敬请留意";
    public static Object object2=new Object();//保存团购订单锁
    public static Object object4=new Object();//更新团购订单锁




    //支付
    public static Object pay_object=new Object();
    public static String openId_isNull="openId为空";
    public static String ip_unright="IP不正确";
    public static String notify_url="https://www.cslapp.com/tuan/pay/notify";
    public static String common_pay_url="https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单url
    public static Map<String,String> tuan_package_map=new HashMap<>();

    //红包
    public static String haved_open="你已帮房主拆过一次了";
    public static String redpacket_is_end="当前红包已失效";
    public static String redpacket_not_open="红包活动未开启";
    public static Object object6=new Object();

    //优惠券
    public static String coupon_expire="优惠券已经过期";
    public static List<String> sendsms3=new ArrayList<>();

    //零元购
    public static String free_time_expired="你未获得零元购机会或已机会失效";
}
