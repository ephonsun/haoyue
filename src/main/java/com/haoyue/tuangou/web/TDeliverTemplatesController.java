package com.haoyue.tuangou.web;

import com.haoyue.tuangou.pojo.TDeliverTemplate;
import com.haoyue.tuangou.pojo.TDeliverTemplates;
import com.haoyue.tuangou.service.TDeliverTemplateService;
import com.haoyue.tuangou.service.TDeliverTemplatesService;
import com.haoyue.tuangou.utils.TGlobal;
import com.haoyue.tuangou.utils.TResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiJia on 2017/11/7.
 */
@RestController
@RequestMapping("/tuan/tdelivertemplates")
public class TDeliverTemplatesController {

    @Autowired
    private TDeliverTemplatesService templatesService;
    @Autowired
    private TDeliverTemplateService templateService;

    // /tuan/tdelivertemplates/save?dname=申通&sendAddress=发送地&priceType=计价方式&deliverType=运送方式&price=默认运费
    // &count=默认件数&moreCount=增加件数&morePrice=增加运费&saleId=卖家Id
    // &lines=行之间用==分隔，格之间=分隔
    @RequestMapping("/save")
    public TResult save(TDeliverTemplates templates,String lines){
        //校验快递模板是否存在
        TDeliverTemplates templates1= templatesService.findBySaleIdAndDname(templates.getSaleId(),templates.getDname());
        if (templates1!=null&&templates1.getId()!=null){
            return new TResult(true,TGlobal.deliver_template_exist,null);
        }
        if (templates.getDname().contains("顺丰")){
            if (!templates.getPriceType().equals("重量")){
                return new TResult(true,TGlobal.weight_required,null);
            }
        }
        //新建模板
        Date date=new Date();
        templates.setCreateDate(date);
        String[] line=lines.split("==");
        List<TDeliverTemplate> list=new ArrayList<>();
        for (int i=0;i<line.length;i++){
            String[] detail=line[i].split("=");
            TDeliverTemplate template=new TDeliverTemplate();
            template.setCreateDate(date);
            template.setDestinationAddress(detail[0]);
            template.setCount(detail[1]);
            template.setPrice(Double.valueOf(detail[2]));
            template.setMoreCount(detail[3]);
            template.setMorePrice(Double.valueOf(detail[4]));
            template.setSaleId(templates.getSaleId());
            templateService.save(template);
            list.add(template);
        }
        templates.setTemplates(list);
        templatesService.save(templates);
        return new TResult(false, TGlobal.do_success,templates);
    }

    //   /tuan/tdelivertemplates/del_templates?saleId=卖家Id&id=12
    @RequestMapping("/del_templates")
    public TResult del_templates(TDeliverTemplates templates){
        TDeliverTemplates tDeliverTemplates=templatesService.findOne(templates.getId());
        if (!templates.getSaleId().equals(tDeliverTemplates.getSaleId())){
            return new TResult(true, TGlobal.have_no_right,null);
        }
        templatesService.del(tDeliverTemplates);
        return new TResult(false, TGlobal.do_success,null);
    }

    //   https://www.cslapp.com/tuan/tdelivertemplates/del_template?saleId=7&id=2
    @RequestMapping("/del_template")
    public TResult del_template(TDeliverTemplate template){
        TDeliverTemplate deliverTemplate=templateService.findOne(template.getId());
        if (!template.getSaleId().equals(deliverTemplate.getSaleId())){
            return new TResult(true, TGlobal.have_no_right,null);
        }
        templateService.del(deliverTemplate);
        return new TResult(false, TGlobal.do_success,null);
    }

    //   http://localhost:8080/tuan/tdelivertemplates/update_template?saleId=2&id=2&destinationAddress=安徽，江苏&price=8&count=8&morePrice=8&moreCount=8
    @RequestMapping("/update_template")
    public TResult update_template(TDeliverTemplate template){
        TDeliverTemplate deliverTemplate=templateService.findOne(template.getId());
        if (!template.getSaleId().equals(deliverTemplate.getSaleId())){
            return new TResult(true, TGlobal.have_no_right,null);
        }
        deliverTemplate.setDestinationAddress(template.getDestinationAddress());
        deliverTemplate.setPrice(template.getPrice());
        deliverTemplate.setCount(template.getCount());
        deliverTemplate.setMorePrice(template.getMorePrice());
        deliverTemplate.setMoreCount(template.getMoreCount());
        templateService.save(deliverTemplate);
        return new TResult(false, TGlobal.do_success,deliverTemplate);
    }

    //   /tuan/tdelivertemplates/list?saleId=12
    @RequestMapping("/list")
    public TResult list(String saleId){
        List<TDeliverTemplates> list= templatesService.findBySaleId(saleId);
        return new TResult(false, TGlobal.do_success,list);
    }

    //   /tuan/tdelivertemplates/add_template?templatesId=1&destinationAddress=目的地&price=首件运费&count=首件数&morePrice=增加运费&moreCount=增加件数&saleId=1
    @RequestMapping("/add_template")
    public TResult add_template(TDeliverTemplate template,String templatesId){
        template.setCreateDate(new Date());
        templateService.save(template);
        TDeliverTemplates templates=templatesService.findOne(Integer.parseInt(templatesId));
        List<TDeliverTemplate> oldlist=templates.getTemplates();
        List<TDeliverTemplate> newlist=new ArrayList<>();
        newlist.addAll(oldlist);
        newlist.add(template);
        templates.setTemplates(newlist);
        templatesService.save(templates);
        return new TResult(false, TGlobal.do_success,templates);
    }
}
