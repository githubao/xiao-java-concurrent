package org.xiao.concurrent.headin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生产者消费者
 *
 * @author BaoQiang
 * @version 2.0
 * @date: 2017/6/22 20:50
 */

public class ProducerConsumer {
    public ProducerConsumer() {
    }

    public void go() {
        List<Task> buffer = new ArrayList<>(Constants.MAX_BUFFER_SIZE);
        ExecutorService service = Executors.newFixedThreadPool(Constants.NUM_OF_CONSUMER + Constants.NUM_OF_PRODUCER);
        for (int i = 0; i < Constants.NUM_OF_PRODUCER; i++) {
            service.execute(new Producer(buffer));
        }
        for (int i = 0; i < Constants.NUM_OF_CONSUMER; i++) {
            service.execute(new Consumer(buffer));
        }
    }

    public static void main(String[] args) {
        new ProducerConsumer().go();
    }

}

class Constants {
    public static final int MAX_BUFFER_SIZE = 10;
    public static final int NUM_OF_PRODUCER = 2;
    public static final int NUM_OF_CONSUMER = 3;
    public static final int NUM_OF_TASK = 100;
}

class Task {
    private String id;

    public Task() {
        this.id = UUID.randomUUID().toString();
    }

    public Task(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Task[%s]", id);
    }
}

class Consumer implements Runnable {
    private List<Task> buffer;

    public Consumer(List<Task> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (buffer) {
                while (buffer.isEmpty()) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Task task = buffer.remove(0);
                buffer.notifyAll();

//                模拟耗时的处理操作
                int elapsed = (int) (Math.random() * 10);
                try {
                    Thread.sleep(elapsed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(String.format("Consumer[%s] get %s, elapsed %s", Thread.currentThread().getName(), task, elapsed));
            }
        }
    }
}

class Producer implements Runnable {
    private static int currentTaskNum = 0;

    private List<Task> buffer;

    public Producer(List<Task> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (buffer) {
                while (buffer.size() >= Constants.MAX_BUFFER_SIZE) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (currentTaskNum < Constants.NUM_OF_TASK) {
                    Task task = new Task(String.valueOf(++currentTaskNum));
                    buffer.add(task);
                    buffer.notifyAll();
                    System.out.println(String.format("Producer[%s] put %s", Thread.currentThread().getName(), task));
                } else {
                    System.out.println("Task Down");
                    System.exit(0);
                }

            }
        }
    }
}