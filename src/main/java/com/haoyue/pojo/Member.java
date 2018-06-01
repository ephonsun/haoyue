package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/10/31.
 * 会员
 * 没有openId是卖家设置的会员等级规则
 * 不同的会员对应同的优惠折扣
 */
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue
    private Integer id;

    private String openId;
    private String sellerId;
    private String code;//会员编号
    private String discount;//折扣
    private String wxname;
    private String phone;
    private String birthday;// 10-01
    private String sex;
    private String groupName;//会员对应分组
    private String email;
    private String leavel;//普通会员 高级会员 至尊会员  lev1 lev2 lev3 lev4
    private double total_consume;//最低消费  会员消费
    private double avg_consume;//平均消费额
    private int nums;//购买次数
    private int closenums;//关闭订单次数
    private int productnums;//宝贝件数
    private String froms;//来源 交易成功  交易未成功
    private String pic;
    private String defultPic;//默认图片
    private String receiveAddress;
    private String province;
    private String city;
    private boolean active = true;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;//创建日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date latestBuyDate;//上次购买日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;//生日日期


    public String getDefultPic() {
        return defultPic;
    }

    public void setDefultPic(String defultPic) {
        this.defultPic = defultPic;
    }

    public String getFroms() {
        return froms;
    }

    public void setFroms(String froms) {
        this.froms = froms;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getAvg_consume() {
        return avg_consume;
    }

    public void setAvg_consume(double avg_consume) {
        this.avg_consume = avg_consume;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public int getProductnums() {
        return productnums;
    }

    public void setProductnums(int productnums) {
        this.productnums = productnums;
    }

    public int getClosenums() {
        return closenums;
    }

    public void setClosenums(int closenums) {
        this.closenums = closenums;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLatestBuyDate() {
        return latestBuyDate;
    }

    public void setLatestBuyDate(Date latestBuyDate) {
        this.latestBuyDate = latestBuyDate;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }


    public double getTotal_consume() {
        return total_consume;
    }

    public void setTotal_consume(double total_consume) {
        this.total_consume = total_consume;
    }

    public String getLeavel() {
        return leavel;
    }

    public void setLeavel(String leavel) {
        this.leavel = leavel;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
