package com.haoyue.hywebsite;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by LiJia on 2018/1/12.
 */
@RestController
@RequestMapping("/website/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;


    //  /website/agent/save?companyName=公司名&companyProvince=公司省份&floatMoney=流动资金（数字类型）&preferCity=意向代理城市
    //   &manageScope=经营范围&employeeNum=员工数(数字类型)&agentPast=过往代理&CustomerSourceDescribe=客户资源描述
    //   &planEnjoys=从事业务人员&salesNum=销售人数(数字类型)&manager=负责人&managerPhone=手机号
    @RequestMapping("/save")
    public Result save(Agent agent) {
        if (agentService.findByManagerPhone(agent.getManagerPhone())!=null){
            return new Result(true, "当前手机号已注册", null);
        }
        agent.setCreateDate(new Date());
        agentService.save(agent);
        return new Result(false, "success", null);
    }

    //  /website/agent/del?id=代理商ID
    @RequestMapping("/del")
    public Result del(int id){
        agentService.del(id);
        return new Result(false, "success", null);
    }

    //  /website/agent/list?pageNumber=0
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, String> map, @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Iterable<Agent> iterable = agentService.list(map, pageNumber, pageSize);
        return new Result(false, "success", iterable);
    }

    //  /website/agent/findone?id=代理商ID
    @RequestMapping("/findone")
    public Result findOne(int id,String phone){
        if (id!=0) {
            return new Result(false, "success", agentService.findOne(id));
        }
        if (phone!=null&&!phone.equals("")){
            Agent agent=agentService.findByManagerPhone(phone);
            if (agent!=null){
                return new Result(true, "当前手机号已注册",null);
            }
        }
        return new Result(false, "success", null);
    }

    //  /website/agent/phonecode?phone=手机号  返回验证码，再和用户输入的验证码对比
    @RequestMapping("/phonecode")
    public Result getPhoneCode(String phone) {
        phone = phone.trim();
        //校验手机号码
        if (phone == null || phone.equals("")) {
            return new Result(true, "手机号不能为空", null);
        }
        if (phone.length() != 11) {
            return new Result(true, "手机号应该为11位数字", null);
        }
        for (char i : phone.toCharArray()) {
            if (!Character.isDigit(i)) {
                return new Result(true, "手机号应该为11位数字", null);
            }
        }
        String code = getCode();
        try {
            //发送验证码
            SendCode.sendSms(phone, code);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new Result(false, "success", code);
    }


    public String getCode() {
        String str = "0123456789";
        int index = 0;
        String code = "";
        for (int i = 0; i < 4; i++) {
            index = (int) (Math.random() * str.length());
            code += str.charAt(index);
        }
        return code;
    }

}
