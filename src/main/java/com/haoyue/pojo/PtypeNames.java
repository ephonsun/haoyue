package com.haoyue.pojo;

import javax.persistence.*;
import java.util.List;

/**
 * Created by LiJia on 2017/10/26.
 * 用于存放商品一级分类  因为这样可以改变一级分类的顺序，每次商品有变动的时候刷新一下商品分类
 */
@Entity
@Table(name = "ptypenames")
public class PtypeNames {

    @Id
    @GeneratedValue
    private Integer id;
    private String sellerId;
    private String ptypename;//一级分类
    private String ptypenames;//二级分类 (废弃)


    public String getPtypenames() {
        return ptypenames;
    }

    public void setPtypenames(String ptypenames) {
        this.ptypenames = ptypenames;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPtypename() {
        return ptypename;
    }

    public void setPtypename(String ptypename) {
        this.ptypename = ptypename;
    }
}
