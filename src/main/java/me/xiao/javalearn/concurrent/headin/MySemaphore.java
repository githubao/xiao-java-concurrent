package me.xiao.javalearn.concurrent.headin;

import java.util.concurrent.Semaphore;

/**
 * 并发信号互斥量
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/23 10:25
 */

public class MySemaphore {
    public static final int NUM_OF_FORKS = 5;
    public static final int NUM_OF_PHILO = 5;

    public static Semaphore[] forks;
    public static Semaphore counter;

    static {
        forks = new Semaphore[NUM_OF_FORKS];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Semaphore(1);
        }

//        如果有N个哲学家，只能有N-1个人取得叉子
        counter = new Semaphore(NUM_OF_PHILO - 1);
    }

    public void go() {
        String[] names = {"小包", "小强", "小赵", "小李", "小青"};
        for (int i = 0; i < names.length; i++) {
            new Thread(new Philosopher(i, names[i])).start();
        }
    }

    public static void main(String[] args) {
        new MySemaphore().go();
    }

    public static void putOnFork(int index, boolean leftFirst) throws InterruptedException {
        if (leftFirst) {
            forks[index].acquire();
            forks[(index + 1) % NUM_OF_PHILO].acquire();
        } else {
            forks[(index + 1) % NUM_OF_PHILO].acquire();
            forks[index].acquire();
        }
    }

    public static void putDownFork(int index, boolean leftFirst) throws InterruptedException {
        if (leftFirst) {
            forks[index].release();
            forks[(index + 1) % NUM_OF_PHILO].release();
        } else {
            forks[(index + 1) % NUM_OF_PHILO].release();
            forks[index].release();
        }
    }

}

class Philosopher implements Runnable {
    private int index;
    private String name;

    public Philosopher(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MySemaphore.counter.acquire();
                boolean leftFirst = index % 2 == 0;
                MySemaphore.putOnFork(index, leftFirst);
                System.out.println(name + "成功取到叉子在吃饭");
                MySemaphore.putDownFork(index, leftFirst);
                MySemaphore.counter.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
