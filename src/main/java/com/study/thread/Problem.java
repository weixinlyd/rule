package com.study.thread;

/**
 * 可见性问题,共享变量将值写入主内存，值才会共享
 */
public class Problem {
    int count = 0;

    public synchronized void add(){
        int tap = count;
        tap+=1;
        count = tap;
        System.out.println(count);
    }

    public static void main(String[] args) {
        Problem p = new Problem();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                p.add();
            }
        });
        t.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                p.add();
            }
        });
        t2.start();
    }
}
