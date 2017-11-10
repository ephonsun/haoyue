package com.haoyue.tuangou.pojo;

import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 * 卖家后台正在拼单结果集封装
 */
public class TuanOrdersResponse {

    private TProducts products;
    private Iterable<TuanOrders> iterable;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TProducts getProducts() {
        return products;
    }

    public void setProducts(TProducts products) {
        this.products = products;
    }


    public Iterable<TuanOrders> getIterable() {
        return iterable;
    }

    public void setIterable(Iterable<TuanOrders> iterable) {
        this.iterable = iterable;
    }
}
