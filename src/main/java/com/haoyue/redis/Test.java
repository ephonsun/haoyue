//package com.haoyue.redis;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.haoyue.pojo.Seller;
//import net.sf.json.JSONArray;
//import redis.clients.jedis.Jedis;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by LiJia on 2017/10/30.
// */
//public class Test {
//
//    /**
//     * 第一步：maven依赖
//     * org.springframework.boot:spring-boot-starter-redis
//     * org.springframework:spring-context-support
//     * org.springframework.data:spring-data-redis
//     * redis.clients:jedis
//     * org.apache.commons:commons-pool2
//     * 第二步：配置文件 application.properties
//     *
//     * # REDIS (RedisProperties)
//     * # Redis数据库索引（默认为0）
//     * spring.redis.database=0
//     * # Redis服务器地址
//     * spring.redis.host=127.0.0.1
//     * # Redis服务器连接端口
//     * spring.redis.port=6379
//     * # Redis服务器连接密码（默认为空）
//     * spring.redis.password=
//     * # 连接池最大连接数（使用负值表示没有限制）
//     * spring.redis.pool.max-active=8
//     * # 连接池最大阻塞等待时间（使用负值表示没有限制）
//     * spring.redis.pool.max-wait=-1
//     * # 连接池中的最大空闲连接
//     * spring.redis.pool.max-idle=8
//     * # 连接池中的最小空闲连接
//     * spring.redis.pool.min-idle=0
//     * # 连接超时时间（毫秒）
//     * spring.redis.timeout=2000
//     *
//     * 第三步：本地redis下载安装后开启
//     *
//     * 第四步使用，主要是使用 Jedis对 redis 进行操作
//     */
//
//    public void f1() throws IOException {
//        Seller seller = new Seller();
//        /**
//         * 填充数据
//         */
//        // key - value(string)
//        Jedis redis = new Jedis("localhost", 6379);
//        redis.set("key1", "abc");
//        redis.get("key1");
//        // key - value(object)
//        redis.set("key2".getBytes(), SerializeUtil.serialize(seller));
//        Object object = SerializeUtil.unserializeList(redis.get("key2".getBytes()));
//        seller = (Seller) object;
//        // key - value(List<object>)
//        List<Seller> sellerList = new ArrayList<>();
//        //方法一
//        JSONArray jsonArray = new JSONArray();
//        for (Seller seller1 : sellerList) {
//            jsonArray.add(seller1);
//        }
//        redis.set("key3", jsonArray.toString());
//        //方法二
//        String data = new ObjectMapper().writeValueAsString(sellerList);
//        redis.set("key3", data);
//
//        String value = redis.get("key3");
//        ObjectMapper mapper = new ObjectMapper();
//        List<Object> list = mapper.readValue(value, List.class);
//
//    }
//}
