//package com.haoyue.redis;
//
///**
// * Created by LiJia on 2017/10/30.
// */
//import redis.clients.jedis.Jedis;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SerializeUtil {
//
//    private  static  Jedis redis = new Jedis("localhost", 6379);
//
//    public static byte[] serialize(Object object) {
//        ObjectOutputStream oos = null;
//        ByteArrayOutputStream baos = null;
//        try {
//            // 序列化
//            baos = new ByteArrayOutputStream();
//            oos = new ObjectOutputStream(baos);
//            oos.writeObject(object);
//            byte[] bytes = baos.toByteArray();
//            return bytes;
//        } catch (Exception e) {
//
//        }
//        return null;
//    }
//
//    public static Object unserialize( byte[] bytes) {
//        ByteArrayInputStream bais = null;
//        try {
//            // 反序列化
//            bais = new ByteArrayInputStream(bytes);
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            return ois.readObject();
//        } catch (Exception e) {
//
//        }
//        return null;
//    }
//
//    /**
//     * 序列化 list 集合
//     *
//     * @param list
//     * @return
//     */
//    public static byte[] serializeList(List<?> list) {
//
//        ObjectOutputStream oos = null;
//        ByteArrayOutputStream baos = null;
//        byte[] bytes = null;
//        try {
//            baos = new ByteArrayOutputStream();
//            oos = new ObjectOutputStream(baos);
//            for (Object obj : list) {
//                oos.writeObject(obj);
//            }
//            bytes = baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(oos);
//            close(baos);
//        }
//        return bytes;
//    }
//
//    /**
//     * 反序列化 list 集合
//     *
//     * @param
//     * @return
//     */
//    public static List<?> unserializeList(byte[] bytes) {
//        if (bytes == null) {
//            return null;
//        }
//
//        List<Object> list = new ArrayList<Object>();
//        ByteArrayInputStream bais = null;
//        ObjectInputStream ois = null;
//        try {
//            // 反序列化
//            bais = new ByteArrayInputStream(bytes);
//            ois = new ObjectInputStream(bais);
//            while (bais.available() > 0) {
//                Object obj = (Object) ois.readObject();
//                if (obj == null) {
//                    break;
//                }
//                list.add(obj);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(bais);
//            close(ois);
//        }
//        return list;
//    }
//
//    /**
//     * 关闭io流对象
//     *
//     * @param closeable
//     */
//    public static void close(Closeable closeable) {
//        if (closeable != null) {
//            try {
//                closeable.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 设置List集合
//     * @param key
//     * @param list
//     */
//    public static void setList(String key ,List<?> list){
//        try {
//
//            if(list.size()!=0){
//                getJedis().set(key.getBytes(), SerializeUtil.serializeList(list));
//            }else{
//                //如果list为空,则设置一个空
//                getJedis().set(key.getBytes(), "".getBytes());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 获取List集合
//     * @param key
//     * @return
//     */
//    public static List<?> getList(String key){
//        if(getJedis() == null || !getJedis().exists(key)){
//            return null;
//        }
//        byte[] data = getJedis().get(key.getBytes());
//        return SerializeUtil.unserializeList(data);
//    }
//
//    public static  Jedis getJedis(){
//        return redis;
//    }
//}
//
