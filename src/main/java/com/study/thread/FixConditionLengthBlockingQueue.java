package com.study.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程
 */
public class FixConditionLengthBlockingQueue<T> {

    private final int length;

    private final T[] items;

    private int head,tail,count;

    ReentrantLock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public FixConditionLengthBlockingQueue(int length) {
        this.length = length;
        this.items = (T[]) new Object[length];
    }
    //判断是否为堵塞状态,如果线程已满，则为堵塞状态
    public void put(T t) throws InterruptedException {
        lock.lock();
        try{
            while (count==length){
                notFull.await();
            }
            items[head] = t;
            if(++head>length){
                head=0;
            }
            count++;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }
    //判断是否为堵塞状态,如果线程为空,则为堵塞状态
    public T take() throws InterruptedException {
        T t = null;
        lock.lock();
        try{
            while (count==0){
                notFull.await();
            }
            t = items[tail];
            if(++tail>=length){
                tail=0;
            }
            count--;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        final FixConditionLengthBlockingQueue<String> queue = new FixConditionLengthBlockingQueue<>(1);
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
