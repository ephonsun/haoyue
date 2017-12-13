package com.haoyue.tuangou.service;

import com.haoyue.tuangou.pojo.QTOrders;
import com.haoyue.tuangou.pojo.TOrders;
import com.haoyue.tuangou.repo.TOrdersRepo;
import com.haoyue.tuangou.utils.StringUtils;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import com.haoyue.untils.OSSClientUtil;
import com.querydsl.core.BooleanBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by LiJia on 2017/11/9.
 */
@Service
public class TOrdersService {

    @Autowired
    private TOrdersRepo tOrdersRepo;

    public void save(TOrders orders) {
        tOrdersRepo.save(orders);
    }

    public TOrders findOne(int id) {
        return tOrdersRepo.findOne(id);
    }

    public void update(TOrders orders) {
        tOrdersRepo.save(orders);
    }

    public Iterable<TOrders> list(Map<String, String> map, int pageNumber, int pageSize) {
        QTOrders orders = QTOrders.tOrders;
        BooleanBuilder bd = new BooleanBuilder();
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("saleId")) {
                    bd.and(orders.saleId.eq(value));
                    bd.and(orders.showsale.eq(true));
                } else if (name.equals("openId")) {
                    bd.and(orders.openId.eq(value));
                    bd.and(orders.showbuy.eq(true));
                } else if (name.equals("state")) {
                    bd.and(orders.state.eq(value));
                } else if (name.equals("code")) {
                    bd.and(orders.code.eq(value));
                } else if (name.equals("showsale")) {
                    bd.and(orders.showsale.eq(true));
                }

            }
        }

        return tOrdersRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }

    public TOrders findByCode(String code) {
        return tOrdersRepo.findByCode(code);
    }

    public Iterable<TOrders> clist(String saleId, String openId, String str) {
        QTOrders order = QTOrders.tOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(order.saleId.eq(saleId));
        bd.and(order.openId.eq(openId));
        if (!StringUtils.isNullOrBlank(str)) {
            if (str.equals("unsend")) {
                bd.and(order.state.eq(TGlobal.order_unsend));
            }
            if (str.equals("unreceive")) {
                bd.and(order.state.eq(TGlobal.order_unreceive));
            }
            if (str.equals("finsh")) {
                bd.and(order.state.eq(TGlobal.order_finsh));
            }
        } else {
            bd.and(order.state.eq(TGlobal.order_unpay));
        }
        bd.and(order.showbuy.eq(true));
        return tOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TOrders> comments(Map<String, String> map) {
        QTOrders order = QTOrders.tOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(order.iscomment.eq(true));
        bd.and(order.tProducts.id.eq(Integer.parseInt(map.get("pid"))));
        return tOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TOrders> query(Map<String, String> map) {
        QTOrders order = QTOrders.tOrders;
        BooleanBuilder bd = new BooleanBuilder();
        Date from = null;
        Date end = null;
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (!StringUtils.isNullOrBlank(value)) {
                if (key.equals("code")) {
                    bd.and(order.code.contains(value));
                }
                if (key.equals("saleId")) {
                    bd.and(order.saleId.eq(value));
                }
                if (key.equals("startDate")) {
                    try {
                        from = StringUtils.formatStrToDate((map.get("startDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (key.equals("endDate")) {
                    try {
                        end = StringUtils.formatStrToDate((map.get("endDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (from != null && end != null) {
                    bd.and(order.createDate.between(from, end));
                }
                if (key.equals("wxname")) {
                    bd.and(order.wxname.contains(value));
                }
            }
        }
        return tOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<TOrders> commentslist(Map<String, String> map) {
        QTOrders order = QTOrders.tOrders;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(order.iscomment.eq(true));
        bd.and(order.saleId.eq(map.get("saleId")));
        return tOrdersRepo.findAll(bd.getValue(), new Sort(Sort.Direction.DESC, "id"));
    }

    // 系统自动确认收货 20 天
    public void autofinsh() {
        Date date = new Date();
        List<TOrders> list = tOrdersRepo.findByState(TGlobal.order_unreceive);
        for (TOrders order : list) {
            long start = order.gettDeliver().getSendDate().getTime();
            //延迟收货 7 日
            if (order.getIsdelay()) {
                start += 3600 * 24 * 7 * 1000;
            }
            if (date.getTime() - start > 3600 * 1000 * 24 * 20) {
                order.setState(TGlobal.order_finsh);
                save(order);
            }
        }
    }


public TResult excel(String saleId, String state) throws IOException {

    //2007 及以上excel
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("sheet1");
    //第一行 列名
    XSSFRow row = sheet.createRow(0);
    Cell cell = row.createCell(0);
    cell.setCellValue("姓名");
    cell = row.createCell(1);
    cell.setCellValue("电话");
    cell = row.createCell(2);
    cell.setCellValue("地址");
    cell = row.createCell(3);
    cell.setCellValue("款号");
    cell = row.createCell(4);
    cell.setCellValue("颜色");
    cell = row.createCell(5);
    cell.setCellValue("尺码");
    cell = row.createCell(6);
    cell.setCellValue("数量");
    cell = row.createCell(7);
    cell.setCellValue("卖家备注");
    cell = row.createCell(8);
    cell.setCellValue("买家备注");
    cell = row.createCell(9);
    cell.setCellValue("订单状态");

    //需要转excel的订单
    List<TOrders> list=new ArrayList<>();
    if (StringUtils.isNullOrBlank(state)) {
        list = tOrdersRepo.findBySaleId(saleId);
    }else {
        list = tOrdersRepo.findBySaleIdAndState(saleId,state);
    }

    if (list.size()!=0){
        //倒序
        Collections.reverse(list);
        //行号
        int rowindex=1;
        String name="";
        String phone="";
        String address="";
        String model="";
        String color="";
        String size="";
        String amount="";
        String buyComment="";
        String sellerComment="";
        for (TOrders order:list){

            //获取数据
            name=order.gettDeliver().getReceiver();
            phone=order.gettDeliver().getPhone();
            address=order.gettDeliver().getAddress();
            model=order.gettProducts().getStyle();
            color=order.gettProductsTypes().getColor();
            size=order.gettProductsTypes().getSize();
            amount=String.valueOf(order.getAmount());
            buyComment=order.getLeavemsg();
            sellerComment=order.getLeavemsg2();
            //校验数据
            if (StringUtils.isNullOrBlank(name)||StringUtils.isNullOrBlank(phone)||StringUtils.isNullOrBlank(address))
            {
                continue;
            }
            if (name.equals("undefined")||phone.equals("undefined")||address.equals("undefined")){
                continue;
            }

            //填充数据
            row = sheet.createRow(rowindex++);
            cell = row.createCell(0);
            cell.setCellValue(name);
            cell = row.createCell(1);
            cell.setCellValue(phone);
            cell = row.createCell(2);
            cell.setCellValue(address);
            cell = row.createCell(3);
            cell.setCellValue(model);
            cell = row.createCell(4);
            cell.setCellValue(color);
            cell = row.createCell(5);
            cell.setCellValue(size);
            cell = row.createCell(6);
            cell.setCellValue(amount);
            cell = row.createCell(7);
            cell.setCellValue(buyComment);
            cell = row.createCell(8);
            cell.setCellValue(sellerComment);
            cell = row.createCell(9);
            cell.setCellValue(order.getState());
        }
    }

    //获取项目根路径
    String relativelyPath = System.getProperty("user.dir");
    //把excel文件写入 haoyue/excel/ 文件夹下
    String filename = relativelyPath + "/excel/" + new Date().getTime()  + ".xlsx";
    String mkdis = relativelyPath + "/excel/";
    File file1 = new File(mkdis);
    if (!file1.isDirectory()) {
        file1.mkdirs();
    }
    FileOutputStream out = new FileOutputStream(new File(filename));
    workbook.write(out);
    out.close();
    //缓存文件
    File file = new File(filename);
    //上传到阿里云，并返回文件外链
    OSSClientUtil ossClientUtil = new OSSClientUtil();
    FileInputStream inputStream = new FileInputStream(file);
    filename = filename.substring(filename.lastIndexOf("/") + 1);
    filename = "excel/" + filename;
    try {
        ossClientUtil.uploadFile2OSS(inputStream, filename, null);
    } catch (Exception e) {
        e.printStackTrace();
        return new TResult(true, TGlobal.server_busying, null);
    }
    //删除缓存文件
    file.delete();
    return new TResult(false,TGlobal.do_success,TGlobal.aliyun_href + filename);
}

    public void updateWxname(String openId, String wxname) {
        tOrdersRepo.updateWxname(openId,wxname);
    }

    public void updateWxpic(String openId, String wxpic) {
        tOrdersRepo.updateWxpic(openId,wxpic);
    }
}
