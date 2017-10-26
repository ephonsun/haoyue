package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/21.
 */
//卖家
@Entity
@Table(name = "sellers")
public class Seller {

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
    private String authority;//权限 0 简易版 1 基础版  2 高级版

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

}
