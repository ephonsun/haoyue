package com.haoyue;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by LiJia on 2017/10/16.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MyTest {

    public static void main(String[] args){

//        myThread myThread1=new myThread();
//        myThread myThread2=new myThread();
//        myThread1.setName("myThread1111111");
//        myThread2.setName("myThread2222222");
//        myThread1.start();
//        myThread2.start();

//        myThreadRunable myThreadRunable1=new myThreadRunable();
//        myThreadRunable myThreadRunable2=new myThreadRunable();
//        Thread thread1=new Thread(myThreadRunable1);
//        thread1.setName("thread111111111111");
//        Thread thread2=new Thread(myThreadRunable2);
//        thread2.setName("thread222222222222");
//
//        thread1.start();
//        thread2.start();
//        try {
//            thread1.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Thread myThread = new myThread();
                 for (int i = 0; i < 100; i++) {
                        System.out.println("main thread i = " + i);
                        if (i == 20) {
                                  myThread.setDaemon(true);
                                  myThread.start();
                             }
                      }


    }

}

class myThread extends Thread{

    int i=0;

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("i = " + i);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class myThreadRunable implements  Runnable{

    int i=0;

    @Override
    public void run() {
        while (i<100){
            System.out.println(Thread.currentThread().getName()+"====="+i++);
        }
    }
}
