package com.haoyue.service;

import com.haoyue.pojo.*;
import com.haoyue.repo.OrderRepo;
import com.haoyue.untils.Global;
import com.haoyue.untils.OSSClientUtil;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
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
import java.util.*;

/**
 * Created by LiJia on 2017/8/24.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MemberService memberService;

    public Iterable<Order> list(Map<String, String> map, int pageNumber, int pageSize) {

        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();
        boolean flag = false;
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("id")) {
                    bd.and(order.id.eq(Integer.parseInt(value)));
                }
                if (name.equals("token")) {
                    Date now = new Date();
                    Date befor = new Date();
                    befor.setDate(1);
                    befor.setHours(0);
                    befor.setMinutes(0);
                    befor.setSeconds(0);
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                    bd.and(order.createDate.between(befor, now));
                    bd.and(order.state.notIn(Global.order_unpay));
                }
                if (name.equals("state")) {
                    bd.and(order.state.eq(value));
                }
                if (name.equals("sellerId")) {
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("isApplyReturn")) {
                    bd.and(order.isApplyReturn.eq(Boolean.valueOf(value)));
                }
                if (name.equals("luckdraw")) {
                    bd.and(order.isLuckDraw.eq(true));
                    bd.and(order.state.ne(Global.order_luckdraw_unpay));
                    flag = true;
                }
                if (name.equals("active")){
                    bd.and(order.active.eq(Boolean.valueOf(value)));
                }
                if (name.equals("luck")) {
                    bd.and(order.isLuck.eq(true));
                }
            }
        }
        if (flag) {
            int count = orderRepo.findLuckDrawSize(map.get("sellerId"));
            return orderRepo.findAll(bd.getValue(), new PageRequest(pageNumber, count, new Sort(Sort.Direction.DESC, new String[]{"createDate"})));
        }
        return orderRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, new String[]{"createDate"})));
    }

    public Iterable<Order> clist(Map<String, String> map) {

        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();

        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("cid")) {
                    bd.and(order.customerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("state")) {
                    if (!value.equals("全部订单")) {
                        bd.and(order.state.contains(value));
                    }
                }
                if (name.equals("sellerId")) {
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
                if (name.equals("active")) {
                    bd.and(order.active.eq(Boolean.valueOf(value)));
                }
                if (name.equals("luckdraw")) {
                    bd.and(order.isLuckDraw.eq(true));
                }

            }
        }
        return orderRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public Order findOne(Integer id) {
        return orderRepo.findOne(id);
    }

    public void del(Integer id) {
        orderRepo.delete(id);
    }

    public void update(Order order) {
        orderRepo.save(order);
    }

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> findUnDone(String order_send) {
        return orderRepo.findByState(order_send);
    }


    public double getToTalPriceByCustomerId(String cid, String sellerId, Date date) {
        return orderRepo.getToTalPriceBySellerId(cid, sellerId, date);
    }

    public Date getdate() {
        Date date = new Date();
        date.setMonth(0);
        date.setDate(1);
        date.setHours(0);
        date.setMinutes(0);
        return date;
    }

    // 卖家更新会员系统后，刷新所有买家会员信息
    public void getToTalPriceByCustomer(String sellerId, List<Member> news) {
        // 查询该年至今所有买家的交易额
        List<OrderTotalPrice> list = orderRepo.getToTalPriceBySellerAndCustomer(sellerId, getdate());
        String total_consume_1 = "";
        String discount_1 = "";
        String total_consume_2 = "";
        String discount_2 = "";
        String total_consume_3 = "";
        String discount_3 = "";
        int index = 0;
        int customer_id = 0;
        String openId = "";
        String result_discount = "";
        for (Member m : news) {
            if (m.getLeavel().equals("lev1")) {
                total_consume_1 = m.getTotal_consume();
                discount_1 = m.getDiscount();
            }
            if (m.getLeavel().equals("lev2")) {
                total_consume_2 = m.getTotal_consume();
                discount_2 = m.getDiscount();
            }
            if (m.getLeavel().equals("lev3")) {
                total_consume_3 = m.getTotal_consume();
                discount_3 = m.getDiscount();
            }
        }
        // 遍历每一个买家的交易额和会员成长体系进行对比
        for (OrderTotalPrice each : list) {
            if (each.getTotal_price() >= Double.valueOf(total_consume_1)) {
                index = 1;
                result_discount = discount_1;
            }
            if (each.getTotal_price() >= Double.valueOf(total_consume_2)) {
                index = 2;
                result_discount = discount_2;
            }
            if (each.getTotal_price() >= Double.valueOf(total_consume_3)) {
                index = 3;
                result_discount = discount_3;
            }
            // 新增会员，更新老会员
            if (index != 0) {
                customer_id = each.getCustomerId();
                openId = customerService.findOpenIdById(customer_id + "");
                Member member = memberService.findByOpenIdAndSellerId(sellerId, openId);
                if (member == null && member.getId() == null) {
                    member = new Member();
                    member.setSellerId(sellerId);
                    member.setCreateDate(new Date());
                    member.setOpenId(openId);
                    member.setTotal_consume(each.getTotal_price() + "");
                }
                member.setDiscount(result_discount);
                member.setLeavel("lev" + index);
                memberService.save(member);
                if (StringUtils.isNullOrBlank(member.getCode())) {
                    member.setCode((8888 + member.getId()) + "");
                    memberService.save(member);
                }
            }
            //删除老会员
            if (index == 0) {
                Member member = memberService.findByOpenIdAndSellerId(sellerId, openId);
                if (member != null && member.getId() != null) {
                    memberService.del(member);
                }
            }
        }
    }

    public List<String> findBySellerIdAndProIdAndIsLuckDrawEnd(Integer sellerId, Integer pid) {
        List<Integer> oids = orderRepo.findBySellerIdAndProIdAndIsLuckDrawEnd(pid);
        List<String> codes = new ArrayList<>();
        for (Integer id : oids) {
            Order order = orderRepo.findOne(id);
            if (order.getIsLuckDraw() == true && order.getIsLuckDrawEnd() == false) {
                codes.add(order.getLuckcode());
            }
        }
        return codes;
    }

    public void updateIsLuckDrawEnd(Integer pid) {
        List<Integer> oids = orderRepo.findBySellerIdAndProIdAndIsLuckDrawEnd(pid);
        for (Integer id : oids) {
            Order order = orderRepo.findOne(id);
            if (order.getIsLuckDrawEnd() == false) {
                order.setIsLuckDrawEnd(true);
                if (order.getIsLuck()) {
                    //中奖-待发货
                    order.setState(Global.order_unsend);
                } else {
                    //未中奖-已完成
                    order.setState(Global.order_finsh);
                }
                orderRepo.save(order);
            }
        }
    }

    public List<Order> findBySellerIdAndCreateDate(String sellerId, Date from, Date end) {
        return orderRepo.findBySellerIdAndCreateDate(sellerId, from, end);
    }

    public void updateIsLuckDrawEndBySeller(String sellerId) {
        orderRepo.updateIsLuckDrawEndBySeller(sellerId);
    }

    public List<String> findByLuckCodeBySeller(Integer sellerId) {
        return orderRepo.findByLuckCodeBySeller(sellerId);
    }


    public Result excel(String sellerId,String oids) throws IOException {

        if (StringUtils.isNullOrBlank(oids)){
            return new Result(true,Global.do_fail,Global.data_unright,null);
        }

        //2007 及以上excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        //第一行 列名
        XSSFRow row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("下单时间");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("电话");
        cell = row.createCell(3);
        cell.setCellValue("款号");
        cell = row.createCell(4);
        cell.setCellValue("颜色");
        cell = row.createCell(5);
        cell.setCellValue("尺码");
        cell = row.createCell(6);
        cell.setCellValue("数量");
        cell = row.createCell(7);
        cell.setCellValue("地址");
        cell = row.createCell(8);
        cell.setCellValue("卖家备注");
        cell = row.createCell(9);
        cell.setCellValue("买家备注");

        //需要转excel的订单
        List<Order> list=new ArrayList<>();
        String[] id=oids.split("=");
        for (int i=0;i<id.length;i++){
            if (StringUtils.isNullOrBlank(id[i])){
                continue;
            }
            list.add(findOne(Integer.parseInt(id[i])));
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
            for (Order order:list){

               //获取数据
                name=order.getAddress().getReceiver();
                phone=order.getAddress().getPhone();
                address=order.getAddress().getAddress();
                model=order.getProducts().get(0).getModel();
                color=order.getProdutsTypes().get(0).getColor();
                size=order.getProdutsTypes().get(0).getSize();
                amount=String.valueOf(order.getAmount());
                buyComment=order.getLeaveMessage();
                sellerComment=order.getLeaveMessage_seller();
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
                cell.setCellValue(StringUtils.formDateToStr(order.getCreateDate()));

                cell = row.createCell(1);
                cell.setCellValue(name);

                cell = row.createCell(2);
                cell.setCellValue(phone);

                cell = row.createCell(3);
                cell.setCellValue(model);

                cell = row.createCell(4);
                cell.setCellValue(color);

                cell = row.createCell(5);
                cell.setCellValue(size);

                cell = row.createCell(6);
                cell.setCellValue(amount);

                cell = row.createCell(7);
                cell.setCellValue(address);

                cell = row.createCell(8);
                cell.setCellValue(buyComment);

                cell = row.createCell(9);
                cell.setCellValue(sellerComment);

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
            return new Result(true, Global.server_busying, null, null);
        }
        //删除缓存文件
        file.delete();
        return new Result(false,Global.do_success,Global.aliyun_href + filename, null);
    }


    public Iterable<Order> filter(Map<String, String> map) {
        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(order.active.eq(true));
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("ordercode")) {
                    bd.and(order.orderCode.contains(value));
                }
                else if (name.equals("pname")){
                    bd.and(order.products.any().pname.contains(value));
                }
                else if (name.equals("sellerId")){
                    bd.and(order.sellerId.eq(Integer.parseInt(value)));
                }
                else if (name.equals("pcode")){
                    bd.and(order.products.any().pcode.contains(value));
                }
                else if (name.equals("state")){
                    bd.and(order.state.contains(value));
                }
                else if (name.equals("wxname")){
                    bd.and(order.wxname.contains(value));
                }
            }
        }
        return orderRepo.findAll(bd.getValue(),new Sort(Sort.Direction.DESC,"id"));
    }

    public Iterable<Order> comments(String sellerId,int pageNumber,int pageSize) {
        QOrder order = QOrder.order;
        BooleanBuilder bd = new BooleanBuilder();
        bd.and(order.state.eq(Global.order_finsh));
        bd.and(order.comment.isNotNull());
        bd.and(order.sellerId.eq(Integer.parseInt(sellerId)));
        return orderRepo.findAll(bd.getValue(),new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC,"id")));
    }

    public void autoDone(Integer id) {
        orderRepo.autoDone(id);
    }

    public  List<Order> findUnComment(String openId, String sellerId) {
        Customer customer= customerService.findByOpenId(openId,sellerId);
        return orderRepo.findUnComment(customer.getId(),sellerId);
    }
}
