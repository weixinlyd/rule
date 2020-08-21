package com.study.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {
    static int count = 0;
    static int failcut = 0;


    public void syncDemo() {
        synchronized (this) {

        }
    }

    /**
     * 显示锁
     */
    static Lock lock = new ReentrantLock();

    public void lockDemo() {
        try {
            lock.lock();
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

    /**
     * 相当于同步锁
     */
    public static void add() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static void limitlock() throws InterruptedException {
        boolean locked = lock.tryLock(0, TimeUnit.MILLISECONDS);
        //非阻塞的，不需要等直接走
        //boolean locked = lock.tryLock();
        if(locked){
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }else{
            //没拿到锁处理锁失败逻辑
            failcut++;
        }

    }

    public static void intruput() throws InterruptedException {
        //非阻塞的，不需要等直接走
        int times = 3;
        for(int i=0;i<times;i++){
            boolean locked = lock.tryLock();
            if(locked){
                try {
                    count++;
                } finally {
                    lock.unlock();
                }
                return;
            }
        }
        //没拿到锁处理锁失败逻辑
        failcut++;


    }


    public static void main(String[] args) {
        ExecutorService es = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 10);
        List<Callable<String>> list = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            list.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
//                    add();
                    //多线程问题  不等于10万
//                    count++;
                    //限时
//                    limitlock();
                    //非阻塞
                    intruput();
                    return "ok";
                }
            });
        }
        try {
            es.invokeAll(list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("count:"+count);
        System.out.println("fail:"+failcut);
        es.shutdown();
//        for(int i=0;i<100000;i++){
//            count++;
//        };
//        System.out.println(count);
    }
}
