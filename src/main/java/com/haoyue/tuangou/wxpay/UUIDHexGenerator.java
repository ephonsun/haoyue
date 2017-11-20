package com.haoyue.tuangou.wxpay;

/**
 * Created by LiJia on 2017/9/12.
 */

import java.net.InetAddress;


public class UUIDHexGenerator {
    private static String sep = "";
    private static int IP;
    private static int counter = 0;
    private static int JVM = (int) (System.currentTimeMillis());
    private static UUIDHexGenerator uuidgen = new UUIDHexGenerator();

    static {
        int ipadd;
        try {
            ipadd = toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }

    public static UUIDHexGenerator getInstance() {
        return uuidgen;
    }

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int value;
            value = (int) (((bytes[i] & 0xFF) << 24)
                    | ((bytes[i + 1] & 0xFF) << 16)
                    | ((bytes[i + 2] & 0xFF) << 8)
                    | (bytes[i + 3] & 0xFF));
            result = result + value;
        }
        return result;
    }

    protected static String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected static String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    protected static int getJVM() {
        return JVM;
    }

    protected synchronized static int getCount() {
        if (counter < 0) {
            counter = 0;
        }
        counter = counter + 1;
        return counter;
    }

    protected static int getIP() {
        return IP;
    }

    protected static short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    protected static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    public static String generate() {
//        System.out.println("getIp()="+format(getIP()));
//        System.out.println("getJVM()="+format(getJVM()));
//        System.out.println("getHiTime()="+format(getHiTime()));
//        System.out.println("getLoTime()="+format(getLoTime()));
//        System.out.println("getCount()="+format(getCount()));

//        return new StringBuffer(30).append(format(getIP())).append(sep).append(format(getJVM())).append(sep)
//                .append(format(getHiTime())).append(sep).append(format(getLoTime())).append(sep)
//                .append(format(getCount())).toString();

        return new StringBuffer(30).append(format(getJVM())).append(sep)
                .append(format(getHiTime())).append(sep).append(format(getLoTime())).append(sep).append(format(getCount())).toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String id = "";
        UUIDHexGenerator uuid = UUIDHexGenerator.getInstance();
        /*
        for (int i = 0; i < 100; i++) {
            id = uuid.generate();
        }*/
        id = uuid.generate();
        System.out.println(id);

//        getIp()=00000000
//        getJVM()=74a0ae1f
//        getHiTime()=015e
//        getLoTime()=74a0ae2e
//        getCount()=00000000
//        0000000074a0ae1f015e74a0ae2e00000001
    }
}
