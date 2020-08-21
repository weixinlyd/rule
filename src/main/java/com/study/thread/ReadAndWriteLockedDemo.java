package com.study.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWriteLockedDemo {

    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readWriteLock = lock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    final Lock l = new ReentrantLock();
    private int count;


    public int getCount(){
        readWriteLock.lock();
        try{
            return count;
        }finally {
            readWriteLock.unlock();
        }
    }

    public void setCount(int count){
        writeLock.lock();
        try{
            this.count = count;
        }finally {
            writeLock.unlock();
        }
    }
}
