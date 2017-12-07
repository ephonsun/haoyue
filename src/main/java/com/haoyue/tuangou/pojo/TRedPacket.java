package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LiJia on 2017/12/6.
 * 红包
 */
@Entity
@Table(name = "t_redpacket")
public class TRedPacket {

    @Id
    @GeneratedValue
    private Integer id;

    private String groupCode;
    private String saleId;
    private String openId;
    private String wxname;
    private String wxpic;
    private int money;//金额
    private boolean isowener=false;//房主
    private String formId;
    private String formId2;
    private String opener;//开红包用户 用户1，用户2，用户3
    private boolean isover=false;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date endDate;//结束日期


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    public String getWxpic() {
        return wxpic;
    }

    public void setWxpic(String wxpic) {
        this.wxpic = wxpic;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean getIsowener() {
        return isowener;
    }

    public void setIsowener(boolean isowener) {
        this.isowener = isowener;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormId2() {
        return formId2;
    }

    public void setFormId2(String formId2) {
        this.formId2 = formId2;
    }

    public String getOpener() {
        return opener;
    }

    public void setOpener(String opener) {
        this.opener = opener;
    }

    public boolean getIsover() {
        return isover;
    }

    public void setIsover(boolean isover) {
        this.isover = isover;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
