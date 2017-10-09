package com.haoyue.pojo;

import javax.persistence.*;

/**
 * Created by LiJia on 2017/8/24.  物流
 */
@Entity
@Table(name = "deliver")
public class Deliver {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true,length = 50)
    private String dcode;//快递号
    @Column(length = 20)
    private String dname;//快递名
    private String dename;//快递名 英文
    private Double price;//运费

    public String getDename() {
        return dename;
    }

    public void setDename(String dename) {
        this.dename = dename;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }
}
