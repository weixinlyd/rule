package com.study.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程
 */
public class FixLengthBlockingQueue<T> {

    private final int length;

    private final T[] items;

    private int head,tail,count;

    public FixLengthBlockingQueue(int length) {
        this.length = length;
        this.items = (T[]) new Object[length];
    }
    //判断是否为堵塞状态,如果线程已满，则为堵塞状态
    public void put(T t) throws InterruptedException {
        synchronized (this){
            while (count==length){
                this.wait();
            }
            items[head] = t;
            if(++head>length){
                head=0;
            }
            count++;
            this.notifyAll();
        }
    }
    //爬蛋是否为堵塞状态,如果线程为空,则为堵塞状态
    public T take() throws InterruptedException {
        T t = null;
        synchronized (this){
            while (count==0){
                this.wait();
            }
            t = items[tail];
            if(++tail>=length){
                tail=0;
            }
            count--;
            this.notifyAll();
        }
        System.out.println("TAKE:"+new Date());
        return t;
    }

    public static void main(String[] args) {
        final FixLengthBlockingQueue<String> queue = new FixLengthBlockingQueue<>(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    queue.put("1");
                    TimeUnit.SECONDS.sleep(2);
                    queue.put("2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("TAKE:"+new Date());
    }
}
