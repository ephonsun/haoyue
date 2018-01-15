package com.haoyue.tuangou.pojo;

import java.util.List;

/**
 * Created by LiJia on 2017/11/9.
 * 卖家后台正在拼单结果集封装
 * 一个商品对应多个拼团房主订单
 */
public class TuanOrdersResponse {

    private TProducts products;//拼团商品
    private Iterable<TuanOrders> iterable;//当前商品对应的房主订单集合
    private int amount;//当前商品的拼团房间数

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
