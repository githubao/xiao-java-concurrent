package org.xiao.concurrent.headin;

import java.util.List;
import java.util.concurrent.*;

/**
 * Future and Callable
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/22 18:48
 */

public class MyFuture {
    public MyFuture() {
    }


    public void go() {
        ExecutorService service = new ThreadPoolExecutor(4, 10, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));

        List<Future<String>> resultList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 10; i++) {
            MyCallTask task = new MyCallTask(String.valueOf(i));
            Future<String> itemResult = service.submit(task);
            resultList.add(itemResult);
        }

        for (Future<String> itemResult : resultList) {
            try {
                System.out.println(itemResult.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        service.shutdown();
        System.out.println("task done");

    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        new MyFuture().go();

        long elapsed = System.currentTimeMillis() - start;
        System.out.println(String.format(" total elapsed %s(ms)", elapsed));
    }

}

class MyCallTask implements Callable<String> {
    private String src;

    public MyCallTask() {
    }

    public MyCallTask(String src) {
        this.src = src;
    }


    @Override
    public String call() throws Exception {
        int elapsed = (int) (Math.random() * 1000);
        Thread.sleep(elapsed);
        return String.format("%s elapsed %s", src, elapsed);
    }
}
