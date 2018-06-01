package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/21.
 * 卖家实体对象
 */

@Entity
@Table(name = "sellers")
public class Seller implements Serializable{

    @Id
    @GeneratedValue
    private Integer sellerId;
    @Column(unique = true,length = 20)
    private String sellerName;
    private String sellerPass;
    @Column(unique = true,length = 20)
    private String sellerPhone;
    @Column(unique = true,length = 30)
    private String sellerEmail;
    private String sellerPic;//店铺头像
    @Lob
    @Column(columnDefinition="TEXT")
    private String banners;//店铺图片
    private Integer maxFileSize;//存储空间 以 kb 为单位
    private Integer uploadFileSize;
    private String videos;//店铺视频

    @Column(unique = true)
    private String appId;
    private String secret;//8bcdb74a9915b5685fa0ec37f6f25b24
    private String authority;//权限 0 简易版 1 基础版  2 高级版
    private String file_payback;//退款证书  apiclient_cert.p12

    private boolean isActive=true;//店铺是否可用
    private boolean iscoupon=false;//优惠券活动是否开启

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date expireDate;//服务到期日期
    private String indexTitle;//首页转发标题
    private String pallTitle;//全部商品转发标题
    private String pdescTitle;//商品详情转发标题
    private String lunbo;//店铺图片对应的商品编号
    private String onlineCode;//在线凭证
    private boolean isMember=false;
    private String mchId;//商户号
    private String key1;//支付密匙
    private String receiveAddress;//退货时候卖家收货地址
    private String price_down_template;//降价通知 HsbxE0x_CqdmCu6u0hhYtGB4Ry2f_R9M96KBLLxWbUM
    private String unpay_template;//待付款通知 Foc6pkGtA2FZON2_5nXJxCmFvfIdKWAv5oj6REd_3w4
    private String payback_template;//退款通知 Foc6pkGtA2FZON2_5nXJxCmFvfIdKWAv5oj6REd_3w4
    private String service_template;//服务状态通知 Foc6pkGtA2FZON2_5nXJxCmFvfIdKWAv5oj6REd_3w4
    private String service_template_msg;
    private String paysuccess_template;//付款成功通知 Z_Xg6rYdQgci4FP_aOjTvZHXeC5BSs99EwARD6NJXWk
    private String customeCard_template;//自定义优惠券通知 custome_card_template
    //积分
    private int maxable;//单次消费最大限度可使用积分
    private double integral_money;// 1积分=金额


    public String getCustomeCard_template() {
        return customeCard_template;
    }

    public void setCustomeCard_template(String customeCard_template) {
        this.customeCard_template = customeCard_template;
    }

    public int getMaxable() {
        return maxable;
    }

    public void setMaxable(int maxable) {
        this.maxable = maxable;
    }

    public double getIntegral_money() {
        return integral_money;
    }

    public void setIntegral_money(double integral_money) {
        this.integral_money = integral_money;
    }

    public String getService_template_msg() {
        return service_template_msg;
    }

    public void setService_template_msg(String service_template_msg) {
        this.service_template_msg = service_template_msg;
    }

    public String getPrice_down_template() {
        return price_down_template;
    }

    public void setPrice_down_template(String price_down_template) {
        this.price_down_template = price_down_template;
    }

    public String getUnpay_template() {
        return unpay_template;
    }

    public void setUnpay_template(String unpay_template) {
        this.unpay_template = unpay_template;
    }

    public String getPayback_template() {
        return payback_template;
    }

    public void setPayback_template(String payback_template) {
        this.payback_template = payback_template;
    }

    public String getService_template() {
        return service_template;
    }

    public void setService_template(String service_template) {
        this.service_template = service_template;
    }

    public String getPaysuccess_template() {
        return paysuccess_template;
    }

    public void setPaysuccess_template(String paysuccess_template) {
        this.paysuccess_template = paysuccess_template;
    }

    public String getSellerPic() {
        return sellerPic;
    }

    public void setSellerPic(String sellerPic) {
        this.sellerPic = sellerPic;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getFile_payback() {
        return file_payback;
    }

    public void setFile_payback(String file_payback) {
        this.file_payback = file_payback;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(boolean member) {
        isMember = member;
    }

    public String getOnlineCode() {
        return onlineCode;
    }

    public void setOnlineCode(String onlineCode) {
        this.onlineCode = onlineCode;
    }

    public String getLunbo() {
        return lunbo;
    }

    public void setLunbo(String lunbo) {
        this.lunbo = lunbo;
    }

    public boolean getIscoupon() {
        return iscoupon;
    }

    public void setIscoupon(boolean iscoupon) {
        this.iscoupon = iscoupon;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }

    public String getPallTitle() {
        return pallTitle;
    }

    public void setPallTitle(String pallTitle) {
        this.pallTitle = pallTitle;
    }

    public String getPdescTitle() {
        return pdescTitle;
    }

    public void setPdescTitle(String pdescTitle) {
        this.pdescTitle = pdescTitle;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public Integer getUploadFileSize() {
        return uploadFileSize;
    }

    public void setUploadFileSize(Integer uploadFileSize) {
        this.uploadFileSize = uploadFileSize;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getBanners() {
        return banners;
    }

    public void setBanners(String banners) {
        this.banners = banners;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPass() {
        return sellerPass;
    }

    public void setSellerPass(String sellerPass) {
        this.sellerPass = sellerPass;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", sellerPass='" + sellerPass + '\'' +
                ", sellerPhone='" + sellerPhone + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", banners='" + banners + '\'' +
                ", maxFileSize=" + maxFileSize +
                ", uploadFileSize=" + uploadFileSize +
                ", videos='" + videos + '\'' +
                ", appId='" + appId + '\'' +
                ", authority='" + authority + '\'' +
                ", isActive=" + isActive +
                ", iscoupon=" + iscoupon +
                ", expireDate=" + expireDate +
                ", indexTitle='" + indexTitle + '\'' +
                ", pallTitle='" + pallTitle + '\'' +
                ", pdescTitle='" + pdescTitle + '\'' +
                ", lunbo='" + lunbo + '\'' +
                ", onlineCode='" + onlineCode + '\'' +
                '}';
    }
}
