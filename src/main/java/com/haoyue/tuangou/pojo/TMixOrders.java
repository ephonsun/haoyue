package com.haoyue.tuangou.pojo;

import java.util.Date;

/**
 * Created by LiJia on 2017/11/13.
 * 用于将普通订单和团购订单混在一起，按照时间排序
 */
public class TMixOrders {

    private int oid;
    private Date date;
    private boolean istuan=false;

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean istuan() {
        return istuan;
    }

    public void setIstuan(boolean istuan) {
        this.istuan = istuan;
    }
}
