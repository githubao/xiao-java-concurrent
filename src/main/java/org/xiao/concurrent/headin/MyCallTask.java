package org.xiao.concurrent.headin;

import java.util.concurrent.Callable;

/**
 * callable的任务
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/22 19:17
 */

public class MyCallTask implements Callable<String> {
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