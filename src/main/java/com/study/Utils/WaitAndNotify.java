package com.study.Utils;

import java.util.concurrent.TimeUnit;

/**
 * 线程方法原理测试
 */
public class WaitAndNotify {
    public static void main(String[] args) {
        Object co = new Object();
        System.out.println(co);

        for (int i = 0; i < 5; i++) {
            MyThread t = new MyThread("Thread" + i, co);
            //启动线程方法,run方法不会创建新线程,而wait()方法会创建一个新的线程在启动run方法
            //该例子在遍历创建了5个新的线程并且让线程进入等待池,
            //验证时会发现执行run方法不会往下执行，因为没有线程,而wait()方法会继续执行，因为创建了新的线程。
            t.run();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("-----Main Thread notify-----");
            synchronized (co) {
                //notify()方法会随机唤醒等待池的一个线程,而换成notify()方法会将所有等待池的线程加入锁池中
                co.notify();
            }

            TimeUnit.SECONDS.sleep(2);
            System.out.println("Main Thread is end.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyThread extends Thread {
        private String name;
        private Object co;

        public MyThread(String name, Object o) {
            this.name = name;
            this.co = o;
        }

        @Override
        public void run() {
            System.out.println(name + " is waiting.");
            try {
                //同步锁,在同一时间执行一个线程
                synchronized (co) {
                    //将线程加入等待池,
                    co.wait();
                }
                System.out.println(name + " has been notified.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
