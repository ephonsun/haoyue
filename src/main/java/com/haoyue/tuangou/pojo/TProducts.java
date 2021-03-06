package com.haoyue.tuangou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 * 商品
 */
@Entity
@Table(name = "t_products")
public class TProducts {

    @Id
    @GeneratedValue
    private Integer id;
    private String saleId;
    private String pname;
    private String style;
    private String types;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String indexPic;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String detailPic;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TProductsTypes> productsTypes;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String parameters;//参数
    private String deliver;
    private boolean isTuan=false;
    private boolean active=true;
    private int tuanNumbers;//开团人数
    private int tuanTimes;//团购时间
    private int saleNum;//总销量
    private int tuanSaleNum;//已拼量
    private String suffix;//价格后缀
    private String qrcode;//二维码
    private double weight;//重量
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createDate;//创建日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;//团购开始日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;//团购结束日期

    private boolean isFree=false;//是否开启0元购
    private boolean isEnd=false;


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getTuanSaleNum() {
        return tuanSaleNum;
    }

    public void setTuanSaleNum(int tuanSaleNum) {
        this.tuanSaleNum = tuanSaleNum;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean end) {
        isEnd = end;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean free) {
        isFree = free;
    }

    public int getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<TProductsTypes> getProductsTypes() {
        return productsTypes;
    }

    public void setProductsTypes(List<TProductsTypes> productsTypes) {
        this.productsTypes = productsTypes;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getIndexPic() {
        return indexPic;
    }

    public void setIndexPic(String indexPic) {
        this.indexPic = indexPic;
    }

    public String getDetailPic() {
        return detailPic;
    }

    public void setDetailPic(String detailPic) {
        this.detailPic = detailPic;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public boolean getIsTuan() {
        return isTuan;
    }

    public void setIsTuan(boolean tuan) {
        isTuan = tuan;
    }

    public int getTuanNumbers() {
        return tuanNumbers;
    }

    public void setTuanNumbers(int tuanNumbers) {
        this.tuanNumbers = tuanNumbers;
    }

    public int getTuanTimes() {
        return tuanTimes;
    }

    public void setTuanTimes(int tuanTimes) {
        this.tuanTimes = tuanTimes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
