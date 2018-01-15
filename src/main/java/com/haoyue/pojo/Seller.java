package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/21.
 */
//卖家
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
    private String template_pay;// 付款通知 Z_Xg6rYdQgci4FP_aOjTvZHXeC5BSs99EwARD6NJXWk
    private String template_daily;//日常通知 Foc6pkGtA2FZON2_5nXJxCmFvfIdKWAv5oj6REd_3w4
    private String template_downprice;// 降价通知 HsbxE0x_CqdmCu6u0hhYtGB4Ry2f_R9M96KBLLxWbUM
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


    public String getTemplate_downprice() {
        return template_downprice;
    }

    public void setTemplate_downprice(String template_downprice) {
        this.template_downprice = template_downprice;
    }

    public String getFile_payback() {
        return file_payback;
    }

    public void setFile_payback(String file_payback) {
        this.file_payback = file_payback;
    }

    public String getTemplate_daily() {
        return template_daily;
    }

    public void setTemplate_daily(String template_daily) {
        this.template_daily = template_daily;
    }

    public String getTemplate_pay() {
        return template_pay;
    }

    public void setTemplate_pay(String template_pay) {
        this.template_pay = template_pay;
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
