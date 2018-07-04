package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lijia on 2018/7/3.
 */
@Entity
@Table(name = "phone_records")
public class PhoneRecords {

    @Id
    @GeneratedValue
    private Integer id;
    private String phone;
    private String sellerId;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @Temporal(TemporalType.DATE)
    @Column(updatable=false)
    private Date createDate;//创建日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "PhoneRecords{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
