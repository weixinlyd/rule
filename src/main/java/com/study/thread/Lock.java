package com.study.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Lock {
    static Object lock = new Object();
    public static int x = 0;

    public synchronized void get(){
        synchronized (this){

        }
    }
    public synchronized static void getstatic(){
        synchronized (Lock.class){

        }
    }
    public static void main(String[] args) throws InterruptedException {
        //互斥性代码块同时只有一个线程能够运行，
        //可见性,代码块中共享变量是最新值
        //1.获取锁
        //2.运行代码块中的代码
        //3.释放锁
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println("锁1"+new Date());
                    //本地内存
                    System.out.println("x:"+x);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int tmp = x;
                    x = tmp+1;

                    //存入主内存
                    System.out.println("x:"+x);

                }
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println("锁2"+new Date());
                    //线程二拿到数值是最新的
                    System.out.println("x:"+x);
                }
            }
        });
        t2.start();
    }
}
