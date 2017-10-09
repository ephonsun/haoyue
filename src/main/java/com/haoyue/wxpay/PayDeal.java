package com.haoyue.wxpay;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by LiJia on 2017/9/19.
 * 付款结果记录
 */
@Entity
@Table(name = "paydeals")
public class PayDeal {

    @Id
    @GeneratedValue
    private Integer id;

    private String transaction_id;
    private String openId;
    private String appId;
    private String mch_id;
    private String total_fee;
    private String return_code;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }
}
