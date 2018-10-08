package com.imooc.miaosha.Mytest;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量的测试
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(5);
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i=0;i<20;i++){
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("accessing:"+index);
                        Thread.sleep((long) (Math.random()*1000));
                        semaphore.release();
                        System.out.println("-----------------"+semaphore.availablePermits());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.execute(runnable);
        }
        executor.shutdown();
    }
}
