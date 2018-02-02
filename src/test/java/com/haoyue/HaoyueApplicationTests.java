package com.haoyue;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;

import com.haoyue.untils.*;
//import io.goeasy.GoEasy;
import com.haoyue.untils.HttpRequest;
import io.goeasy.GoEasy;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.SystemProfileValueSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HaoyueApplicationTests {

    @Test
    public void contextLoads()
    {
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
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());

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

//  dog dog1=new dog("dogA","red");
//  dog dog2=new dog("dogB","black");
//        dog dog3=new dog("dogC","yellow");
//
//        List<dog> list=new ArrayList<>();
//        list.add(dog1);
//        list.add(dog2);
//        list.add(dog3);
//
//        list.forEach((dog each)->System.out.print(each));

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
            int len = 0;
            Charset charset = Charset.forName("gbk");// 创建GBK字符集
            while (true) {
                buffer.clear();//pos=0,limit=capcity，作用是让ichannel从pos开始放数据
                len = fileChannel.read(buffer);
                if (len == -1)//到达文件末尾
                    break;
                buffer.flip();
                System.out.println(charset.decode(buffer));
                buffer.flip();
                fileChannel2.write(buffer);//它们的作用是让ochanel写入pos - limit之间的数据
                // 2100
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
    public void f16() {
        GoEasy goEasy = new GoEasy("BC-b368734ade974a11937b2b82c5dc6433");
        goEasy.publish("channel1", "HelloHello!");
    }

    @Test
    public void f18() {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,-3);
        System.out.println(calendar.getTime().toLocaleString());
    }

    class response {
        private String access_token;
        private String expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        @Override
        public String toString() {
            return "response{" +
                    "access_token='" + access_token + '\'' +
                    ", expires_in='" + expires_in + '\'' +
                    '}';
        }
    }

    @Test
    public void f17() {
//        GoEasy goEasy = new GoEasy("rest-hangzhou.goeasy.io","BC-b368734ade974a11937b2b82c5dc6433");
//        goEasy.publish("channel1","HelloHello!");
        //"http(s)://<REST Host>”
    }

    @Test
    public void f20() {
        String relativelyPath = System.getProperty("user.dir");
        String url = relativelyPath + "/files/122jpg.jpg";
        System.out.println(url.replace("\\", "/"));
    }


    @Test
    public  void f21() {
        DecimalFormat df = new DecimalFormat("######0");
        String fee = String.valueOf(df.format(1.0));
        System.out.println(fee);
    }

    @Test
    public void f22(){
     //test,test1,test2,test3,test4,test5
        String old="test,test1,test2,test3,test4,test5";
        String news=old.replace("test1,","");
        System.out.println(news);
    }

    @Test
    public void f23()  {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str="2017-12-08 10:10";
        Date date= null;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            System.out.println("时间格式不正确");
        }
        System.out.println(date.toLocaleString());
    }

    @Test
    public void f24(){
        List<String> list=new ArrayList<>();
        if (list.contains("1")){
            System.out.println("111111");
        }
        list.add("1");
        list.add("11");
        list.add("111");
        if (list.contains("1")){
            System.out.println("22222");
        }
    }

    @Test
    public void f25(){
        String access_token="4_m4SXUmYY-g_E0-eUNsJbJcQkGAA03T0r4bwu3BkmcXsbEHnH-XLx9Ceraa7yy3POH4txYK5X4YKBum3uhIACHDoGCYP0RAW6XFm3ueWBQDl3blwa50TOkhbJ3D8akG_6_AY7FPs7FnW7ycnENUVcAIAFMN";
        getminiqrQr(access_token,"43");
    }

    @Test
    public void f26(){
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
        String param1 = "grant_type=client_credential&appid=wxf80175142f3214e1&secret=e0251029d53d21e84a650681af6139b1";
        String access_token = com.haoyue.tuangou.wxpay.HttpRequest.sendPost(access_token_url, param1);
        access_token = access_token.substring(access_token.indexOf(":") + 2, access_token.indexOf(",") - 1);
        System.out.println(access_token);
    }

    @Test
    public void f27(){
        long a=Long.parseLong("1513743237268");
        System.out.println(a);
        Date date=new Date(a);
        System.out.println(date.toLocaleString());
    }

    @Test
    public void f28() {
        String str="ABCDEFA";
        String str2=str.replace("A","a");
        System.out.println(str);
        System.out.println(str2);

    }


    @Test
    public void f29() {
        HashMap<Integer, Integer> integerHashMap = new HashMap<>();
        integerHashMap.put(10,12);
        integerHashMap.put(8,10);
        integerHashMap.put(12,11);
        integerHashMap.put(9,8);
        Set<Map.Entry<Integer, Integer>> entrySet = integerHashMap.entrySet();
        ArrayList<Map.Entry<Integer, Integer>> arrayList = new ArrayList<>(entrySet);
        Collections.sort(arrayList, (a,  b) ->{
            return a.getKey()-b.getKey();
        });
        ArrayList<Object> conList = new ArrayList<>();
        arrayList.forEach(a -> {
            conList.add(a.getKey());
            conList.add(a.getValue());
        });
        conList.forEach(a ->
                System.out.print(a +" ")
        );
    }

    @Test
    public void f30(){
        Map<String,String> map=new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        System.out.println(map.get("1"));
        map.clear();
        System.out.println(map.get("1"));
    }

    @Test
    public void f31(){
        String str="12.523";
        System.out.println(new DecimalFormat("0").format(Double.parseDouble(str)));
    }

    @Test
    public void check(){
        //  https://way.jd.com/jisuapi/query?type=SFEXPRESS&number=925749187579&appkey=您申请的APPKEY
        String code="888212746238226568";
        String url="https://way.jd.com/jisuapi/query";
        String param="type=auto&number="+code+"&appkey=3d1b6253bbfbbfb003aa9ec6a3c2ee0c";
        String result=HttpRequest.sendGet(url,param);
        System.out.println(result);

        try {
            net.sf.json.JSONObject jsonObject=net.sf.json.JSONObject.fromObject(result);
            System.out.println(jsonObject.getJSONObject("result"));
            System.out.println(jsonObject.getJSONObject("result").getString("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Dog> list=new ArrayList<>();
        Dog dog=new Dog();
        dog.setName("aaa");
        list.add(dog);

        if (list.contains(dog)){
            System.out.println("=====");
        }
        else {
            System.out.println("------");
        }


    }


    class Dog {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public Map getminiqrQr(String accessToken,String pid) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token="+accessToken;
            Map<String,Object> param = new HashMap<>();
            param.put("path", "pages/details/details?id=43");
            param.put("width", 430);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);

            //获取项目根路径
            String relativelyPath = "c:";
            String mkdirs=relativelyPath+"/qrcode/";
            String filename=relativelyPath+"/qrcode/"+pid+".jpg";
            File filedirs = new File(mkdirs);
            if (!filedirs.isDirectory()){
                filedirs.mkdirs();
            }
            outputStream = new FileOutputStream(new File(filename));
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}






