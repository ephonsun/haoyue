package com.haoyue.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/8/21.
 * 商品实体对象
 */

@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue
    private Integer id;

    private String pcode;//商品编号

    private Integer sellerId;//卖家Id
    private String sellerName;//卖家姓名
    private String pname;//商品名

    @Lob
    @Column(columnDefinition="TEXT")
    private String pdesc;//描述
    private String deliverPrice;//快递费用
    private String deliever_name;//快递模板名

    private String pvideo;//视频
    private String ptypeName;//宝贝分类 如 女装
    private String secondPtypeName;//二级分类

    @OneToMany
    private List<ProdutsType> produtsTypes;//颜色 尺码  库存
    private String sendAddress;//发货地址

    @Lob
    @Column(columnDefinition="TEXT")
    private String images;//详情图片  多个图片名之间用 == 连接

    @Lob
    @Column(columnDefinition="TEXT")
    private String indexImages;//首页展示图
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @Temporal(TemporalType.DATE)
    @Column(updatable=false)
    private Date createDate;//创建日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date showDate;//发布日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date secondKillStart;//秒杀开始日期

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date secondKillEnd;//秒杀结束日期

    private boolean issecondkill=false;//是否秒杀
    private boolean active=true;//是否上架
    private boolean isdelete=false;

    //宝贝参数
    private String brand;//品牌
    private String pNumber;//货号
    private String pStyle;//风格
    private String pattern;//版型
    private String materialQuality;//材质
    private String model;//款式
    private String designPic;//图案
    private String invoice_type;//发票类型
    private String suffix;//价格后缀

    private int monthSale;//月销量，后台实现总销量代替月销量
    private int thumbsup;//点赞数
    private String dname;
    private Integer shopcar_count;//被加入购物车数量
    private boolean isLuckDraw=false;//是否抽奖
    private boolean isLuckDrawEnd=false;
    private int views;
    private String qrcode;//二维码
    private double weight;//重量

    //多个商品对应一个折扣活动
    @ManyToOne
    private ActivityForDiscount activityForDiscount;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<CustomProductsTypes> parenttypes;//一个商品对应多个一级分类
    @ManyToMany(cascade = CascadeType.ALL)
    private List<CustomProductsTypes> childtypes;//一个商品对应多个二级分类

    public boolean getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    public List<CustomProductsTypes> getParenttypes() {
        return parenttypes;
    }

    public void setParenttypes(List<CustomProductsTypes> parenttypes) {
        this.parenttypes = parenttypes;
    }

    public List<CustomProductsTypes> getChildtypes() {
        return childtypes;
    }

    public void setChildtypes(List<CustomProductsTypes> childtypes) {
        this.childtypes = childtypes;
    }

    public ActivityForDiscount getActivityForDiscount() {
        return activityForDiscount;
    }

    public void setActivityForDiscount(ActivityForDiscount activityForDiscount) {
        this.activityForDiscount = activityForDiscount;
    }

    public String getSecondPtypeName() {
        return secondPtypeName;
    }

    public void setSecondPtypeName(String secondPtypeName) {
        this.secondPtypeName = secondPtypeName;
    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean getIssecondkill() {
        return issecondkill;
    }

    public void setIssecondkill(boolean issecondkill) {
        this.issecondkill = issecondkill;
    }

    public Date getSecondKillStart() {
        return secondKillStart;
    }

    public void setSecondKillStart(Date secondKillStart) {
        this.secondKillStart = secondKillStart;
    }

    public Date getSecondKillEnd() {
        return secondKillEnd;
    }

    public void setSecondKillEnd(Date secondKillEnd) {
        this.secondKillEnd = secondKillEnd;
    }

    public Date getShowDate() {
        return showDate;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean getIsLuckDrawEnd() {
        return isLuckDrawEnd;
    }

    public void setIsLuckDrawEnd(boolean luckDrawEnd) {
        isLuckDrawEnd = luckDrawEnd;
    }

    public boolean getIsLuckDraw() {
        return isLuckDraw;
    }

    public void setIsLuckDraw(boolean luckDraw) {
        isLuckDraw = luckDraw;
    }

    public Integer getShopcar_count() {
        return shopcar_count;
    }

    public void setShopcar_count(Integer shopcar_count) {
        this.shopcar_count = shopcar_count;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDeliever_name() {
        return deliever_name;
    }

    public void setDeliever_name(String deliever_name) {
        this.deliever_name = deliever_name;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public int getThumbsup() {
        return thumbsup;
    }

    public void setThumbsup(int thumbsup) {
        this.thumbsup = thumbsup;
    }

    public String getIndexImages() {
        return indexImages;
    }

    public void setIndexImages(String indexImages) {
        this.indexImages = indexImages;
    }

    public Integer getMonthSale() {
        return monthSale;
    }

    public void setMonthSale(int monthSale) {
        this.monthSale = monthSale;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getpStyle() {
        return pStyle;
    }

    public void setpStyle(String pStyle) {
        this.pStyle = pStyle;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMaterialQuality() {
        return materialQuality;
    }

    public void setMaterialQuality(String materialQuality) {
        this.materialQuality = materialQuality;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDesignPic() {
        return designPic;
    }

    public void setDesignPic(String designPic) {
        this.designPic = designPic;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getPtypeName() {
        return ptypeName;
    }

    public void setPtypeName(String ptypeName) {
        this.ptypeName = ptypeName;
    }

    public List<ProdutsType> getProdutsTypes() {
        return produtsTypes;
    }

    public void setProdutsTypes(List<ProdutsType> produtsTypes) {
        this.produtsTypes = produtsTypes;
    }

    public String getPvideo() {
        return pvideo;
    }

    public void setPvideo(String pvideo) {
        this.pvideo = pvideo;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public String getDeliverPrice() {
        return deliverPrice;
    }

    public void setDeliverPrice(String deliverPrice) {
        this.deliverPrice = deliverPrice;
    }
}
