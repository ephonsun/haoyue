package com.haoyue;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.haoyue.untils.*;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HaoyueApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(new Date().toLocaleString());
    }

    @Test
    public void jiemi() {
        WXAppletUserInfo wxAppletUserInfo = new WXAppletUserInfo();
        String encryptedData = "";
        String iv = "YlU0Fg7wxTacPh1kgARdRw==";
        String session_key = "e6hDqemGTaSBDRm9NpF24A==";
        wxAppletUserInfo.decodeUserInfo(encryptedData, iv, session_key);
    }

    @Test
    public void f3() {
        String appId = "wx2be4cbdc761d212f";
        String code = "";
        String secret = "b252ea6c94816a64f1d177ab0734668c";
        String response = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", "appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code");
        System.out.println(response);
    }
    //customer/getPhone?encryptedData=12&iv=12&

    @Test
    public void f4() {
        System.out.println(Integer.parseInt("我"));
    }

    //获取支付信息
    //@EngineFunction("getPayInformation")
//	public static Map<String, Object> getPayInformation(String orderId) throws AVException, UnsupportedEncodingException, DocumentException {
//		Map<String, Object> reqMap = new TreeMap<String, Object>(
//				new Comparator<String>() {
//					public int compare(String obj1, String obj2) {
//						// 升序排序
//						return obj1.compareTo(obj2);
//					}
//				});
//		if (AVUser.getCurrentUser() != null) {
//			String authDataJson = JSONArray.toJSONString(AVUser.getCurrentUser().get("authData"));
//			JSONObject jsonObject = JSON.parseObject(authDataJson);
//			jsonObject.get("lc_weapp");
//			JSONObject j2 = JSON.parseObject(jsonObject.get("lc_weapp").toString());
//			String openId = (String) j2.get("openid");
//			AVQuery<Order> query = AVObject.getQuery(Order.class);
//			Order order = query.get(orderId);
//			reqMap.put("appid", System.getenv("appid"));
//			reqMap.put("mch_id", System.getenv("mch_id"));
//			reqMap.put("nonce_str", WXPayUtil.getNonce_str());
//			reqMap.put("body", new String(order.getDishesList().toString().getBytes("UTF-8")));
//			reqMap.put("openid", openId);
//			reqMap.put("out_trade_no", order.getObjectId());
//			reqMap.put("total_fee", 1); //订单总金额，单位为分
//			reqMap.put("spbill_create_ip", "192.168.0.1"); //用户端ip
//			reqMap.put("notify_url", System.getenv("notify_url")); //通知地址
//			reqMap.put("trade_type", System.getenv("trade_type")); //trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识
//			String reqStr = WXPayUtil.map2Xml(reqMap);
//			String resultXml = HttpRequest.sendPost(reqStr);
//			System.out.println("微信请求返回:" + resultXml);
//			//解析微信返回串 如果状态成功 则返回给前端
//			if (WXPayUtil.getReturnCode(resultXml) != null && WXPayUtil.getReturnCode(resultXml).equals("SUCCESS")) {
//				//成功
//				Map<String, Object> resultMap = new TreeMap<>(
//						new Comparator<String>() {
//							public int compare(String obj1, String obj2) {
//								// 升序排序
//								return obj1.compareTo(obj2);
//							}
//						});
//				resultMap.put("appId", System.getenv("appid"));
//				resultMap.put("nonceStr", WXPayUtil.getNonceStr(resultXml));//解析随机字符串
//				resultMap.put("package", "prepay_id=" + WXPayUtil.getPrepayId(resultXml));
//				resultMap.put("signType", "MD5");
//				resultMap.put("timeStamp", String.valueOf((System.currentTimeMillis() / 1000)));//时间戳
//				String paySign = WXPayUtil.getSign(resultMap);
//				resultMap.put("paySign", paySign);
//				return resultMap;
//			} else {
//				throw new AVException(999, "微信请求支付失败");
//			}
//		} else {
//			throw new AVException(98, "当前未登录用户");
//		}
//	}


    public void upload() {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = Global.accessKeyId;
        String accessKeySecret = Global.accessKeySecret;
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传
        byte[] content = "Hello OSS".getBytes();
        PutObjectResult putObjectResult = ossClient.putObject("haoyue", "<yourKey>", new ByteArrayInputStream(content));
        System.out.println(putObjectResult);
        // 关闭client
        ossClient.shutdown();
    }

    @Test
    public void f5() {
        Date from = new Date();
        Date to = new Date();
        from.setDate(1);
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        System.out.println(from.toLocaleString());
        System.out.println(to.toLocaleString());
    }

    @Test
    public void f6() {
        int m = -1;
        int n = 10;
        System.out.println(m + n);
    }

    @Test
    public void f7() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nomat = sdf.parse("20170919162825");
//		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date format=sdf2.parse(nomat.toString());
        System.out.print(nomat.toLocaleString());
    }

    @Test
    public void f8() {
        Boolean flag = null;
        System.out.println("flag=" + flag);
        flag = true;
        System.out.println("flag=" + flag);
    }

    @Test
    public void f9() {
        Date date = new Date();
        System.out.println("当前日期==" + date.toLocaleString());
        date.setYear(date.getYear() + 20);
        System.out.println("一年后日期==" + date.toLocaleString());
    }

    @Test
    public void f10() {
        String relativelyPath = System.getProperty("user.dir");
        System.out.println(relativelyPath);
    }

    @Test
    public void f11() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        System.out.println(list.size());
        list.clear();
        System.out.println(list.size());
        list.add("a");
        list.add("b");
        System.out.println(list.size());
    }

    @Test
    public void f12() {
        String str = "http://haoyue.oss-cn-beijing.aliyuncs.com/hymarket/2017/9/14/1505380758254.avi";
        System.out.println(str.substring(str.indexOf("hymarket")));
    }

    @Test
    public void f13() {

//   final String text = "Base64 finally in Java 8!";
//   final String encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
//   System.out.println(encoded);
//   final String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
//   System.out.println(decoded);
//   Arrays.asList( "a", "b", "d" ).forEach((String e) -> System.out.println(e));

//        dog dog1=new dog("dogA","red");
//        dog dog2=new dog("dogB","black");
//        dog dog3=new dog("dogC","yellow");
//
//        List<dog> list=new ArrayList<>();
//        list.add(dog1);
//        list.add(dog2);
//        list.add(dog3);
//
//        list.forEach((dog each)->System.out.print(each));
        List<Person> javaProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
                add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
                add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));
                add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
                add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));
                add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
                add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));
                add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
                add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));
                add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));
            }
        };

        String[] players = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka", "David Ferrer",
                "Roger Federer", "Andy Murray",
                "Tomas Berdych", "Juan Martin Del Potro",
                "Richard Gasquet", "John Isner"};

        //javaProgrammers.forEach((Person p)->System.out.print(p.getFirstName()));
        //javaProgrammers.forEach((Person p)->p.setSalary((int)(p.getSalary()*0.3)+p.getSalary()));
        //javaProgrammers.forEach((Person p)->System.out.println(p.getSalary()));
//         List<Person> list=new ArrayList<>();
//         javaProgrammers.stream()
//                //.filter((Person p)->p.getSalary()>1800)
//                //.filter((Person p)->p.getGender().equals("male"))
//                //.limit(2)
//                 .sorted((Person p1,Person p2)->(p1.getSalary()-p2.getSalary()))
//                 .forEach((Person p)->list.add(p));
//
//        list.forEach((p)->System.out.println(p.getSalary()));

        //Arrays.sort(players,(String s1,String s2)->(s1.length()-s2.length()));
        //Arrays.sort(javaProgrammers,(Person p1,Person p2)->(p1.getSalary()-p2.getSalary()));

        Arrays.asList(players).stream()
                .filter((String s) -> (!s.contains("M")))
                .sorted((String s1, String s2) -> (s1.length() - s2.length()))
                .forEach(s -> System.out.println(s));
    }

    @Test
    public void f14() {
//        encryptedData:"y/OT3nATZZ1l5OVQqLSwaF3weGRLWe7ngFI8bfkJ6V7AJeUVCcPSLbYVgchR7jE3OnNV/qbvi6IwlxU8LPQAdSXy0NurqJQF5hPsSBc5TPuahybR73KgjKW++muRLD5VzT9LHvHExcQf/+bi/STdEhDgxCSjKjDdpNTB62ZjnQg4OtoB+w2T/UmQ6HoyW9IOFES13ikt3qX9J3tKaqgzSQ931DSbXcJ8wcW7qx+d3gsyd9WSnJ8Y8nTwe+m3+y39fHxJfZ9MsnJGT1ZoCn/DFs4psCbPRNGLY5P6WcQDtvypuX/W95tszj8vmHWorJN/I0YkVXVsk9lP6RFMwzJNgwEbpUoZ+9SpKxcv6GmSilmuWr2NLOx4z4MNyEs+TLhMsyBs121Z6c+bbaLhASNne5J8jPxSF8w2P9OlrEf4t+SurC0cucjsVy+XSB4tnjxfk+TRjRF+ZW9KUfOq6G4u61sIo8DZePQRKAmLF/4+QE+DszztHTm1U+OPlRYIzAexjkyn5Pg6JIkOkdE7vqbtN0CZmz/we2+n/9k5ie5Kd6PEpZsjtl2DRrsGnK/Wmjbb81tDKm5TMgh2j2EdFwpWbokOOR2RKlGJDl2Y8CRmUBCL1IcSs+Eg01JFMd0zxkVrwCQFH4Ytnj1V0rgrCysxtvXLvbgOhTzv1FAXbFkap1XSbOwjdIdQA6HMbOi51xyFRFPraZde4N/TrFw2RndnxYUl5IZHwDEx2iyiDGVVAwbdJfnt3GJsRUfmePF/SmLwB9S/eH7EIITTTq4LBB+wq81nKFGQVyK/WGYLUAQogE1/rP6E1E3xFGzaOUv05A9A7ab1GkzrQHoWrfDjegrSJJ9kp7+1+zHO+swdOxyHqKyrXTJ+lO5WPHN4b2DZmv94yatdJME9AiqXfhb5JbFVtK6MbW1YXFMWXqEqhus28ErvtTgihmtiVCEYwHvSHKghMa0giMzBT3vowZwoOR1OX7ECAS7lGJy8tim+mB0ZOnV+7BCdnMYO9KaysUo1UtwCTrDOikNQ6ehv6A7MXE8aEBTqVFTb/GSXknGhyr31mMKuI+4zkLOXMr+CexQ3X7jv+T1m0mPk0SKMR5bqS2EPHJoN+W4+m66qXLvrQHgsUdgTAWOBNwCWaBmB+SHZMkb6xn62My/6z8UydEh05jpOe3uVl6cF6cPLPyDELyAZM5ag7tsHfraF+rCQiZH40kXwdIGhCSw+ZxHt8+fAFxmMKm+7+2rJXinGlHGFKIlfIGSbvdcsQR6soZ+Qa5ZOUsu0o1DEzNMWFnKifvpwEC0oLsUP/CwHJRbZGmZdQTKylAYiECo+ESVOgWgNA0DhUXqLjQKST2AILgebogbmvXF9ux1iDpH3zsnad8nusovpddLWPmrbhRGUqgRHD8Vqhdyx6NZsWqau3mHPsEh/nuV8a9upG1IyAxC/Yjxkbpk43ZlhJSxWxKzYBgteaIVbHt+ByL2XSVPwrDLfEGlm7MSuA0tAagdo16NjLcopxmOHdZiQ7JwYEhAuIsCy6nvjgBi3msSPB1PVD26BexGL3FqOHJAwiAMhm3mj0yzLKMaKxiTz6tkm9La5lLocNXrSdoB/eDIGV7UKxdDrQfmLNtNpR30IIypXGNYel37W3AgvpAc="
//        errMsg:"getWeRunData:ok"
//        iv:"/hRYIbdgR8ZZoGDhZhyL2A=="
        String encryptedData = "y/OT3nATZZ1l5OVQqLSwaF3weGRLWe7ngFI8bfkJ6V7AJeUVCcPSLbYVgchR7jE3OnNV/qbvi6IwlxU8LPQAdSXy0NurqJQF5hPsSBc5TPuahybR73KgjKW++muRLD5VzT9LHvHExcQf/+bi/STdEhDgxCSjKjDdpNTB62ZjnQg4OtoB+w2T/UmQ6HoyW9IOFES13ikt3qX9J3tKaqgzSQ931DSbXcJ8wcW7qx+d3gsyd9WSnJ8Y8nTwe+m3+y39fHxJfZ9MsnJGT1ZoCn/DFs4psCbPRNGLY5P6WcQDtvypuX/W95tszj8vmHWorJN/I0YkVXVsk9lP6RFMwzJNgwEbpUoZ+9SpKxcv6GmSilmuWr2NLOx4z4MNyEs+TLhMsyBs121Z6c+bbaLhASNne5J8jPxSF8w2P9OlrEf4t+SurC0cucjsVy+XSB4tnjxfk+TRjRF+ZW9KUfOq6G4u61sIo8DZePQRKAmLF/4+QE+DszztHTm1U+OPlRYIzAexjkyn5Pg6JIkOkdE7vqbtN0CZmz/we2+n/9k5ie5Kd6PEpZsjtl2DRrsGnK/Wmjbb81tDKm5TMgh2j2EdFwpWbokOOR2RKlGJDl2Y8CRmUBCL1IcSs+Eg01JFMd0zxkVrwCQFH4Ytnj1V0rgrCysxtvXLvbgOhTzv1FAXbFkap1XSbOwjdIdQA6HMbOi51xyFRFPraZde4N/TrFw2RndnxYUl5IZHwDEx2iyiDGVVAwbdJfnt3GJsRUfmePF/SmLwB9S/eH7EIITTTq4LBB+wq81nKFGQVyK/WGYLUAQogE1/rP6E1E3xFGzaOUv05A9A7ab1GkzrQHoWrfDjegrSJJ9kp7+1+zHO+swdOxyHqKyrXTJ+lO5WPHN4b2DZmv94yatdJME9AiqXfhb5JbFVtK6MbW1YXFMWXqEqhus28ErvtTgihmtiVCEYwHvSHKghMa0giMzBT3vowZwoOR1OX7ECAS7lGJy8tim+mB0ZOnV+7BCdnMYO9KaysUo1UtwCTrDOikNQ6ehv6A7MXE8aEBTqVFTb/GSXknGhyr31mMKuI+4zkLOXMr+CexQ3X7jv+T1m0mPk0SKMR5bqS2EPHJoN+W4+m66qXLvrQHgsUdgTAWOBNwCWaBmB+SHZMkb6xn62My/6z8UydEh05jpOe3uVl6cF6cPLPyDELyAZM5ag7tsHfraF+rCQiZH40kXwdIGhCSw+ZxHt8+fAFxmMKm+7+2rJXinGlHGFKIlfIGSbvdcsQR6soZ+Qa5ZOUsu0o1DEzNMWFnKifvpwEC0oLsUP/CwHJRbZGmZdQTKylAYiECo+ESVOgWgNA0DhUXqLjQKST2AILgebogbmvXF9ux1iDpH3zsnad8nusovpddLWPmrbhRGUqgRHD8Vqhdyx6NZsWqau3mHPsEh/nuV8a9upG1IyAxC/Yjxkbpk43ZlhJSxWxKzYBgteaIVbHt+ByL2XSVPwrDLfEGlm7MSuA0tAagdo16NjLcopxmOHdZiQ7JwYEhAuIsCy6nvjgBi3msSPB1PVD26BexGL3FqOHJAwiAMhm3mj0yzLKMaKxiTz6tkm9La5lLocNXrSdoB/eDIGV7UKxdDrQfmLNtNpR30IIypXGNYel37W3AgvpAc=";
        String iv = "/hRYIbdgR8ZZoGDhZhyL2A==";
        String session_key = "43xbxdnTuqVfxhaF6dRgNA==";
        WXAppletUserInfo wxAppletUserInfo = new WXAppletUserInfo();
        String str = wxAppletUserInfo.decodeUserInfo(encryptedData, iv, session_key);
        System.out.println(str);
    }

    @Test
    public void f15() {

        try {
            FileInputStream fileInputStream = new FileInputStream(new File("D:/hou.txt"));
            FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/hou123.txt"));
            FileChannel fileChannel = fileInputStream.getChannel();
            FileChannel fileChannel2 = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10240);
            int len=0;
            Charset charset = Charset.forName("gbk");// 创建GBK字符集
            while(true){
                buffer.clear();//pos=0,limit=capcity，作用是让ichannel从pos开始放数据
                len=fileChannel.read(buffer);
                if(len==-1)//到达文件末尾
                    break;
                buffer.flip();
                System.out.println(charset.decode(buffer));
                buffer.flip();
                fileChannel2.write(buffer);//它们的作用是让ochanel写入pos - limit之间的数据

            }
            fileInputStream.close();
            fileOutputStream.close();
            fileChannel.close();
            fileChannel2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void f16(){

        String str=StringUtils.getPinYinByStr("哇哈哈123");
        System.out.println(str);

    }

}

class Person {
    private String firstName, lastName, job, gender;
    private int salary, age;

    public Person(String firstName, String lastName, String job, String gender, int age, int salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.gender = gender;
        this.salary = salary;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
