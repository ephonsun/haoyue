package com.haoyue.web;

import com.haoyue.pojo.*;
import com.haoyue.pojo.Dictionary;
import com.haoyue.service.*;
import com.haoyue.untils.Global;
import com.haoyue.untils.OSSClientUtil;
import com.haoyue.untils.Result;
import com.haoyue.untils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by LiJia on 2017/8/24.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProdutsTypeService produtsTypeService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private DelievrService delievrService;

    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return new Result(false, "", orderService.list(map, pageNumber, pageSize), map.get("token"));
    }

    @RequestMapping("/del")
    public Result del(Integer id, String openId) {
        Order order = orderService.findOne(id);
        Integer sellerId = order.getSellerId();
        Customer customer = customerService.findByOpenId(openId, sellerId + "");
        if (order.getCustomerId() != customer.getId()) {
            return new Result(true, Global.have_no_right, openId);
        }
        orderService.del(id);
        return new Result(false, Global.do_success, openId);
    }

    @RequestMapping("/cancel")
    public Result cancel(Integer id, String openId) {
        Order order = orderService.findOne(id);
        Integer sellerId = order.getSellerId();
        Customer customer = customerService.findByOpenId(openId, sellerId + "");
        if (customer.getId() != order.getCustomerId()) {
            return new Result(true, Global.have_no_right, openId);
        }
        order.setActive(false);
        orderService.update(order);
        return new Result(false, Global.do_success, null, null);
    }

    @RequestMapping("/clist")
    public Result clist(@RequestParam Map<String, String> map) {
        String openId = map.get("openId");
        String sellerId = map.get("sellerId");
        Customer customer = customerService.findByOpenId(openId, sellerId);
        map.put("cid", customer.getId() + "");
        return new Result(false, "", orderService.clist(map), map.get("token"));
    }

    @RequestMapping("/findOne")
    public Result findOne(Integer oid) {
        Order order = orderService.findOne(oid);
        return new Result(false, Global.do_success, order, null);
    }

    @RequestMapping("/save")
    public Result save(String deliver_price, Integer proId, Integer proTypeId, String sellerId, String receiver, String phone, String address, Integer amount, String openId, String leaveMessage) {
        Customer customer = customerService.findByOpenId(openId, sellerId);
        Order order = new Order();
        //客户
        order.setCustomerId(customer.getId());
        //商品
        Products products = productsService.findOne(proId);
        List<Products> productses = new ArrayList<>();
        productses.add(products);
        order.setProducts(productses);
        //商品分类
        ProdutsType produtsType = produtsTypeService.findOne(proTypeId);
        //判断库存量是否足够
        if (produtsType.getAmount() < amount) {
            return new Result(true, Global.amount_notEnough, null, null);
        }
        List<ProdutsType> produtsTypes = new ArrayList<>();
        produtsTypes.add(produtsType);
        order.setProdutsTypes(produtsTypes);
        //快递
        Deliver deliver = new Deliver();
        // if...else... 兼容新旧方法
        if (!StringUtils.isNullOrBlank(deliver_price)) {
            //根据商品快递模板和买家的地区，筛选出快递费用，并以 deliver_price 传递后后台
            deliver.setPrice(Double.valueOf(deliver_price));
        } else {
            deliver.setPrice(Double.valueOf(products.getDeliverPrice())+0);
        }
        delievrService.save2(deliver);
        order.setDeliver(deliver);
        //买家留言
        order.setLeaveMessage(leaveMessage);
        //地址
        Address address1 = new Address();
        address1.setOpenId(openId);
        address1.setAddress(address);
        address1.setPhone(phone);
        address1.setReceiver(receiver);
        addressService.save2(address1);
        order.setAddress(address1);
        //日期
        order.setCreateDate(new Date());
        //数量
        order.setAmount(amount);
        //发票
        order.setInvoiceType(Global.invoice_type);
        //支付方式
        order.setPayType(Global.pay_type);
        //原价
        order.setOldPrice(produtsType.getPriceOld());
        //现价
        order.setPrice(produtsType.getPriceNew() * amount);
        //订单号 唯一
        synchronized (Global.object) {
            order.setOrderCode(Global.order_code_begin + (Global.count++) + new Date().getTime());
            if (Global.count >= 5000) {
                Global.count = 1;
            }
        }
        //卖家
        order.setSellerId(products.getSellerId());
        //新建订单为未付款订单
        order.setState(Global.order_unpay);
        //总计
        order.setTotalPrice(order.getPrice() + order.getDeliver().getPrice());

        return new Result(false, Global.do_success, orderService.save(order), null);
    }

    @RequestMapping("/changeState")
    public Result changeState(Integer oid, String state) {
        Order order = orderService.findOne(oid);
        order.setState(state);
        // 未付款 - 未发货
        if (state.equals(Global.order_unsend)) {
            //付款日期
            order.setPayDate(new Date());

            //更新 dictionary  全部 交易额 订单数量
            Dictionary dictionary = dictionaryService.findByDateAndSellerId(new Date(), order.getSellerId());
            dictionary.setBuyers(dictionary.getBuyers() + 1);
            dictionary.setTurnover(order.getTotalPrice() + dictionary.getTurnover());
            dictionaryService.update(dictionary);

            //更新商品已经卖出量
            List<Products> list = order.getProducts();
            List<ProdutsType> produtsTypes = order.getProdutsTypes();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    Products products = list.get(i);
                    products.setMonthSale(products.getMonthSale() + order.getAmount());
                    // 更新 单个商品的交易额 订单数
                    Dictionary dictionary1 = dictionaryService.findByProductId(products.getId());
                    dictionary1.setBuyers(dictionary1.getBuyers() + 1);
                    dictionary1.setTurnover(dictionary1.getTurnover() + order.getTotalPrice());
                    dictionaryService.update(dictionary1);
                }
            }
            //更新商品分类存库量
            for (ProdutsType produtsType : produtsTypes) {
                produtsType.setAmount(produtsType.getAmount() - order.getAmount());
            }
            productsService.updateList(list);
            produtsTypeService.update(produtsTypes);
        }
        orderService.update(order);
        return new Result(false, Global.do_success, order, null);
    }

    @RequestMapping("/excel")
    public Result excel(String state, String token) throws IOException {
        //根据订单状态选出需要导出excel文件的订单
        Map<String, String> map = new HashMap<>();
        map.put("state", state);
        map.put("sellerId", token);
        Iterable<Order> iterable = orderService.clist(map);

        //倒序结果集
        Iterator<Order> itreator = iterable.iterator();
        List<Order> list = copyIterator(itreator);
        Collections.reverse(list);

        //2007 及以上excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        //第一行
        XSSFRow row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("订单号");
        cell = row.createCell(1);
        cell.setCellValue("交易金额");
        cell = row.createCell(2);
        cell.setCellValue("返点积分");
        cell = row.createCell(3);
        cell.setCellValue("创建时间");
        cell = row.createCell(4);
        cell.setCellValue("交易时间");
        cell = row.createCell(5);
        cell.setCellValue("收货人");
        cell = row.createCell(6);
        cell.setCellValue("联系方式");
        cell = row.createCell(7);
        cell.setCellValue("商品标题");
        cell = row.createCell(8);
        cell.setCellValue("物流单号");
        cell = row.createCell(9);
        cell.setCellValue("物流公司");
        cell = row.createCell(10);
        cell.setCellValue("订单状态");

        int row_index = 1;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Order order = list.get(i);
                if (order == null) {
                    return new Result(true, Global.data_unexist, null, null);
                }
                //校验
                if (order.getSellerId() != Integer.parseInt(token)) {
                    return new Result(true, Global.have_no_right, null, null);
                }
                //填充数据
                row = sheet.createRow(row_index++);
                //订单号
                cell = row.createCell(0);
                cell.setCellValue(order.getOrderCode());
                //交易金额
                cell = row.createCell(1);
                cell.setCellValue(order.getTotalPrice() + 0);
                //返点积分
                cell = row.createCell(2);
                cell.setCellValue(0);//暂时默认为 0
                //创建时间
                cell = row.createCell(3);
                cell.setCellValue(StringUtils.formDateToStr(order.getCreateDate()));
                //交易时间
                cell = row.createCell(4);
                cell.setCellValue(order.getPayDate() == null ? "" : StringUtils.formDateToStr(order.getPayDate()));
                //收货人
                cell = row.createCell(5);
                cell.setCellValue(order.getAddress().getReceiver() == null ? "" : order.getAddress().getReceiver());
                //联系方式
                cell = row.createCell(6);
                cell.setCellValue(order.getAddress().getPhone() == null ? "" : order.getAddress().getPhone());
                //商品标题
                cell = row.createCell(7);
                cell.setCellValue(order.getProducts().get(0).getPname());
                //物流单号
                cell = row.createCell(8);
                cell.setCellValue(order.getDeliver().getDcode() == null ? "" : order.getDeliver().getDcode());
                //物流公司
                cell = row.createCell(9);
                cell.setCellValue(order.getDeliver().getDname() == null ? "" : order.getDeliver().getDname());
                //订单状态
                cell = row.createCell(10);
                cell.setCellValue(order.getState());
            }
        }
        //获取项目根路径
        String relativelyPath = System.getProperty("user.dir");
        //把excel文件写入 haoyue/excel/ 文件夹下
        String filename = relativelyPath + "/excel/" + new Date().getTime() + token + ".xlsx";
        String mkdis = relativelyPath + "/excel/";
        File file1 = new File(mkdis);
        if (!file1.isDirectory()) {
            file1.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(
                new File(filename));
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
            Global.excel_urls.add("hymarket/" + filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, Global.server_busying, null, null);
        }
        //删除缓存文件
        file.delete();
        return new Result(false, Global.do_success, Global.aliyun_href + filename, null);
    }

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    @RequestMapping("/update")
    public Result update(Order order, String selleId) {
        Order order1 = orderService.findOne(order.getId());
        order1.setLeaveMessage_seller(order.getLeaveMessage_seller());
        orderService.update(order1);
        return new Result(false, Global.do_success, null, null);
    }

}
