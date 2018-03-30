package com.haoyue.service;

import com.haoyue.pojo.Member;
import com.haoyue.pojo.Order;
import com.haoyue.pojo.QMember;
import com.haoyue.repo.MemberRepo;
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

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Created by LiJia on 2017/10/31.
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;

    public void save(Member member) {
        memberRepo.save(member);
    }

    public Member findByOpenIdAndSellerId(String openId, String sellerId) {
        return memberRepo.findByOpenIdAndSellerId(openId, sellerId);
    }

    public void update(String sellerId, String discount) {
        memberRepo.updateDiscount(sellerId, discount);
    }

    public String getDiscount(String sellerId) {
        return memberRepo.getDiscount(sellerId);
    }

    public List<Member> findBySellerIdAndOpenIdIsNull(String sellerId) {
        return memberRepo.findBySellerIdAndOpenIdIsNull(sellerId);
    }

    public void updateMemeber(Member member) {
        memberRepo.save(member);
    }

    public void del(Member member) {
        memberRepo.delete(member);
    }

    public void delVip() {
        memberRepo.delVip();
    }

    public List<Member> findByOpenIdIsNotNull() {
        return memberRepo.findByOpenIdIsNotNull();
    }

    public Member findByOpenIdAndLeavelAndSellerId(String openId, String leavel, String sellerId) {
        return memberRepo.findByOpenIdAndLeavelAndSellerId(openId, leavel, sellerId);
    }

    public void flush(List<Member> news, String sellerId) {
        String discount = "";
        String leavel = "";
        for (Member member : news) {
            discount = member.getDiscount();
            leavel = member.getLeavel();
            memberRepo.flushInfo(discount, leavel, sellerId);
        }
    }

    public Member findBySellerIdAndLeavel(String sellerId, String leavel) {
        return memberRepo.findBySellerIdAndLeavelAndOpenIdIsNull(sellerId, leavel);
    }

    public Iterable<Member> list(Map<String, String> map, int pageNumber, int pageSize) {

        QMember member = QMember.member;
        BooleanBuilder bd = new BooleanBuilder();
        int numsfrom = 0;
        int numsto = 0;
        double expensefrom = 0;
        double expenseto = 0;
        Date datefrom = null;
        Date dateto = null;
        Date birthfrom = null;
        Date birthto = null;
        //筛选openId不为null的数据
        bd.and(member.openId.isNotNull());
        for (String name : map.keySet()) {
            String value = (String) map.get(name);
            if (!(StringUtils.isNullOrBlank(value))) {
                if (name.equals("id")) {
                    bd.and(member.id.eq(Integer.parseInt(value)));
                }
                if (name.equals("groupname")) {
                    bd.and(member.groupName.eq(value));
                } else if (name.equals("wxname")) {
                    bd.and(member.wxname.eq(value));
                } else if (name.equals("createdate")) {
                    Calendar calendar=Calendar.getInstance();
                    calendar.add(Calendar.DATE,-1);
                    calendar.set(Calendar.HOUR,0);
                    calendar.set(Calendar.MINUTE,0);
                    Calendar calendar1=Calendar.getInstance();
                    calendar1.set(Calendar.HOUR,0);
                    calendar1.set(Calendar.MINUTE,0);
                    bd.and(member.createDate.between(calendar.getTime(),calendar1.getTime()));
                } else if (name.equals("sellerId")) {
                    bd.and(member.sellerId.eq(value));
                } else if (name.equals("lev")) {
                    bd.and(member.leavel.eq(value));
                } else if (name.equals("sex")) {
                    bd.and(member.sex.eq(value));
                } else if (name.equals("discount")) {
                    bd.and(member.discount.eq(value));
                } else if (name.equals("nums_from")) {
                    numsfrom = Integer.parseInt(value);
                } else if (name.equals("nums_to")) {
                    numsto = Integer.parseInt(value);
                } else if (name.equals("expense_from")) {
                    expensefrom = Double.valueOf(value);
                } else if (name.equals("expense_to")) {
                    expenseto = Double.valueOf(value);
                } else if (name.equals("datefrom")) {
                    try {
                        datefrom = StringUtils.formatDate2(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (name.equals("dateto")) {
                    try {
                        dateto = StringUtils.formatDate2(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (name.equals("birthfrom")) {
                    try {
                        birthfrom = StringUtils.formatDate2(new Date().getYear() + "-" + value);//2018-10-10
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (name.equals("birthto")) {
                    try {
                        birthto = StringUtils.formatDate2(new Date().getYear() + "-" + value);//2018-10-10
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //消费次数
        if (numsto != 0) {
            bd.and(member.nums.between(numsfrom, numsto));
        }
        //消费额
        if (expenseto != 0) {
            bd.and(member.total_consume.between(expensefrom, expenseto));
        }
        //上次交易时间
        if (datefrom != null && dateto != null) {
            bd.and(member.latestBuyDate.between(datefrom, dateto));
        }
        //生日
        if (birthfrom != null && birthto != null) {
            bd.and(member.birthDate.between(birthfrom, birthto));
        }
        return memberRepo.findAll(bd.getValue(), new PageRequest(pageNumber, pageSize, new Sort(Sort.Direction.DESC, "id")));

    }

    public Member findById(int id) {
        return memberRepo.findOne(id);
    }

    public Result toExcel(String ids) throws IOException {

        if (StringUtils.isNullOrBlank(ids)) {
            return new Result(true, Global.do_fail, Global.data_unright, null);
        }

        //2007 及以上excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        //第一行 列名
        XSSFRow row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("微信昵称");
        cell = row.createCell(1);
        cell.setCellValue("分组");
        cell = row.createCell(2);
        cell.setCellValue("手机");
        cell = row.createCell(3);
        cell.setCellValue("会员级别");
        cell = row.createCell(4);
        cell.setCellValue("交易额");
        cell = row.createCell(5);
        cell.setCellValue("交易次数");
        cell = row.createCell(6);
        cell.setCellValue("最新交易时间");
        cell = row.createCell(7);
        cell.setCellValue("生日");
        cell = row.createCell(8);
        cell.setCellValue("性别");
        cell = row.createCell(9);
        cell.setCellValue("地区");

        //需要转excel的会员
        List<Member> list = new ArrayList<>();
        String[] id = ids.split("=");
        for (int i = 0; i < id.length; i++) {
            if (StringUtils.isNullOrBlank(id[i])) {
                continue;
            }
            list.add(findById(Integer.parseInt(id[i])));
        }
        if (list.size() != 0) {
            //倒序
            Collections.reverse(list);
            //行号
            int rowindex = 1;
            String wxname = "";
            String groupname = "";
            String phone = "";
            String leavel = "";
            String moneys = "";
            String nums = "";
            String latestdate = "";
            String birthday = "";
            String sex = "";
            String city = "";
            for (Member member : list) {
                //获取数据
                wxname = member.getWxname();
                groupname = member.getGroupName();
                phone = member.getPhone();
                leavel = member.getLeavel();
                moneys = member.getTotal_consume() + "";
                nums = member.getNums() + "";
                latestdate = StringUtils.formDateToStr(member.getLatestBuyDate());
                birthday = member.getBirthday();
                city = member.getCity();
                sex = member.getSex();
                if (StringUtils.isNullOrBlank(sex)) {
                    sex = "";
                } else {
                    sex = sex.equals("0") ? "女" : "男";
                }
                if (StringUtils.isNullOrBlank(phone)) {
                    phone = "";
                }
                if (StringUtils.isNullOrBlank(birthday)) {
                    birthday = "";
                }
                if (StringUtils.isNullOrBlank(city)) {
                    city = "";
                }
                //填充数据
                row = sheet.createRow(rowindex++);
                cell = row.createCell(0);
                cell.setCellValue(wxname);

                cell = row.createCell(1);
                cell.setCellValue(groupname);

                cell = row.createCell(2);
                cell.setCellValue(phone);

                cell = row.createCell(3);
                if(leavel.equals("lev1")){
                    leavel="普通会员";
                }
                if(leavel.equals("lev2")){
                    leavel="高级会员";
                }
                if(leavel.equals("lev3")){
                    leavel="vip会员";
                }
                if(leavel.equals("lev4")){
                    leavel="至尊vip会员";
                }
                cell.setCellValue(leavel);

                cell = row.createCell(4);
                cell.setCellValue(moneys);

                cell = row.createCell(5);
                cell.setCellValue(nums);

                cell = row.createCell(6);
                cell.setCellValue(latestdate);

                cell = row.createCell(7);
                cell.setCellValue(birthday);

                cell = row.createCell(8);
                cell.setCellValue(sex);

                cell = row.createCell(9);
                cell.setCellValue(city);

            }
        }

        //获取项目根路径
        String relativelyPath = System.getProperty("user.dir");
        //把excel文件写入 haoyue/excel/ 文件夹下
        String filename = relativelyPath + "/excel/" + new Date().getTime() + ".xlsx";
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
        return new Result(false, Global.do_success, Global.aliyun_href + filename, null);
    }
}
