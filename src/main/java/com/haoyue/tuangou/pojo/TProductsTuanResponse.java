package com.haoyue.tuangou.pojo;

import java.util.List;

/**
 * Created by LiJia on 2017/11/13.
 */
public class TProductsTuanResponse {

    private int amount;
    private List<TuanOrders> tuanOrdersList;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<TuanOrders> getTuanOrdersList() {
        return tuanOrdersList;
    }

    public void setTuanOrdersList(List<TuanOrders> tuanOrdersList) {
        this.tuanOrdersList = tuanOrdersList;
    }
}
