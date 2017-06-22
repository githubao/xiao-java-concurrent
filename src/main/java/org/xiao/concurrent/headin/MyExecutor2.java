package org.xiao.concurrent.headin;


/**
 * executor service 使用submit提交任务
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/22 18:01
 */

import java.util.concurrent.*;

public class MyExecutor2 {
    public MyExecutor2() {
    }


    public void go() {
//        ExecutorService service = Executors.newFixedThreadPool(4);
        ExecutorService service = new ThreadPoolExecutor(4, 10, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));

        Future future = null;

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            future = service.submit(
                    () -> {
                        try {
                            System.out.println(String.format("Thread-%s start", finalI));
                            int elapsed = (int) (Math.random() * 1000);
                            Thread.sleep(elapsed);
                            System.out.println(String.format("Thread-%s end, elapsed %s", finalI, elapsed));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (future.isDone()) {
                service.shutdown();
                System.out.println("task done");
                break;
            }

        }

    }


    public static void main(String[] args) {
        new MyExecutor2().go();
    }

}
