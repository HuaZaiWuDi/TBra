package com.wesmartclothing.tbra.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName CountdownLatchTest
 * @Date 2019/2/28 14:18
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class CountdownLatchTest {

    public static final int threadCount = 5;


    public static void main(String[] args) {
        CountDownLatch mCountDownLatch = new CountDownLatch(threadCount);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount, () -> {
            System.out.println("完成线程：" + Thread.currentThread().getName() + "执行最后操作");
        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(new CountDownThread(mCountDownLatch, i));
        }
        try {
            //这里可以设置超时
//            mCountDownLatch.await(5000, TimeUnit.MILLISECONDS);
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("5个子任务执行完毕,主线程继续执行");
//        executorService.shutdownNow();

        for (int i = 0; i < threadCount; i++) {
            if (i == threadCount - 1) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executorService.submit(new CyclicBarrierThread(cyclicBarrier));
        }

        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(new SemaphoreTest(semaphore, i));
        }
        executorService.shutdown();
    }

}

/**
 * Semaphore翻译成字面意思为 信号量，Semaphore可以控同时访问的线程个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
 * <p>
 * 　　Semaphore类位于java.util.concurrent包下，它提供了2个构造器：
 * <p>
 * <p>
 * public Semaphore(int permits) {          //参数permits表示许可数目，即同时可以允许多少线程进行访问
 * sync = new NonfairSync(permits);
 * }
 * public Semaphore(int permits, boolean fair) {    //这个多了一个参数fair表示是否是公平的，即等待时间越久的越先获取许可
 * sync = (fair)? new FairSync(permits) : new NonfairSync(permits);
 * }
 * <p>
 * 　　下面说一下Semaphore类中比较重要的几个方法，首先是acquire()、release()方法：
 * <p>
 * public void acquire() throws InterruptedException {  }     //获取一个许可
 * public void acquire(int permits) throws InterruptedException { }    //获取permits个许可
 * public void release() { }          //释放一个许可
 * public void release(int permits) { }    //释放permits个许可
 * 　　acquire()用来获取一个许可，若无许可能够获得，则会一直等待，直到获得许可。
 * <p>
 * 　　release()用来释放许可。注意，在释放许可之前，必须先获获得许可。
 * <p>
 * 　　这4个方法都会被阻塞，如果想立即得到执行结果，可以使用下面几个方法：
 * <p>
 * public boolean tryAcquire() { };    //尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false
 * public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException { };  //尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
 * public boolean tryAcquire(int permits) { }; //尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false
 * public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException { }; //尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
 * 　　另外还可以通过availablePermits()方法得到可用的许可数目。
 */
class SemaphoreTest implements Runnable {

    private Semaphore mSemaphore;
    private int i;

    public SemaphoreTest(Semaphore semaphore, int i) {
        mSemaphore = semaphore;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            mSemaphore.acquire();
            System.out.println("开始：" + Thread.currentThread().getName() + "--int :" + i);
            Thread.sleep(2000);
            System.out.println("完成：" + Thread.currentThread().getName() + "--int :" + i);
            mSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("SemaphoreTest:结束");
    }
}


/**
 * 字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。我们暂且把这个状态就叫做barrier，当调用await()方法之后，线程就处于barrier了。
 * <p>
 * 　　CyclicBarrier类位于java.util.concurrent包下，CyclicBarrier提供2个构造器：
 * <p>
 * public CyclicBarrier(int parties, Runnable barrierAction) {
 * }
 * <p>
 * public CyclicBarrier(int parties) {
 * }
 * 　　参数parties指让多少个线程或者任务等待至barrier状态；参数barrierAction为当这些线程都达到barrier状态时会执行的内容。
 * <p>
 * 　　然后CyclicBarrier中最重要的方法就是await方法，它有2个重载版本：
 * <p>
 * public int await() throws InterruptedException, BrokenBarrierException { };
 * public int await(long timeout, TimeUnit unit)throws InterruptedException,BrokenBarrierException,TimeoutException { };
 * 　　第一个版本比较常用，用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务；
 * <p>
 * 　　第二个版本是让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。
 */
class CyclicBarrierThread implements Runnable {
    private CyclicBarrier mCyclicBarrier;

    public CyclicBarrierThread(CyclicBarrier cyclicBarrier) {
        mCyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println("开始：" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
            System.out.println("完成：" + Thread.currentThread().getName() + "等待其他线程");
            mCyclicBarrier.await();
            //测试添加延迟好像没有用啊
            //让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。
//            mCyclicBarrier.await(400, TimeUnit.MILLISECONDS);
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("CyclicBarrierThread:结束");
    }
}


/**
 * 　CountDownLatch类位于java.util.concurrent包下，利用它可以实现类似计数器的功能。比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。
 * <p>
 * 　　CountDownLatch类只提供了一个构造器：
 * <p>
 * 1
 * public CountDownLatch(int count) {  };  //参数count为计数值
 * 　　然后下面这3个方法是CountDownLatch类中最重要的方法：
 * <p>
 * public void await() throws InterruptedException { };   //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
 * public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };  //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
 * public void countDown() { };  //将count值减1
 */

class CountDownThread implements Runnable {

    private CountDownLatch mCountDownLatch;
    private int i;

    public CountDownThread(CountDownLatch countDownLatch, int i) {
        mCountDownLatch = countDownLatch;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("开始：" + Thread.currentThread().getName() + "计数：" + i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("完成：" + Thread.currentThread().getName() + "计数：" + i);
        mCountDownLatch.countDown();

        System.out.println("CountDownThread:结束");
    }
}
