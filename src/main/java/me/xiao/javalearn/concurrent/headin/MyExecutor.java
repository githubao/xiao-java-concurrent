package me.xiao.javalearn.concurrent.headin;


/**
 * executor service 使用execute提交任务
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/22 18:01
 */

import java.util.concurrent.*;

public class MyExecutor extends Thread {
    private int index;

    public MyExecutor(int index) {
        this.index = index;
    }

    public MyExecutor() {
    }

    @Override
    public void run() {
        try {
            System.out.println(String.format("Thread-%s start", this.index));
            int elapsed = (int) (Math.random() * 1000);
            Thread.sleep(elapsed);
            System.out.println(String.format("Thread-%s end, elapsed %s", this.index, elapsed));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void go() {
//        ExecutorService service = Executors.newFixedThreadPool(4);
        ExecutorService service = new ThreadPoolExecutor(4, 10, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));
        for (int i = 0; i < 10; i++) {
            service.execute(new MyExecutor(i));
        }
        System.out.println("task done");
        service.shutdown();
    }


    public static void main(String[] args) {
        new MyExecutor().go();
    }

}
