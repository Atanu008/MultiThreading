package org.atanu.multithreading.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleCountDownLatch {

    private static class DependentService implements Runnable{

        private CountDownLatch countDownLatch;

        public DependentService(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }


        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " has started DependentService ");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(threadName + " has finished DependentService ");
            //Each thread calls countDown() method on task completion.
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // If you countDownLatch greater than noOfWorker then the main thread would wait indefinitely
        // Here making noOfWorker = 4 would make the main thread to wait indefinitely as we have only 3 Worker/DependentService
        int noOfWorker = 3;
        CountDownLatch countDownLatch = new CountDownLatch(noOfWorker);

        ExecutorService executorService = Executors.newFixedThreadPool(noOfWorker);
        executorService.submit(new DependentService(countDownLatch));
        executorService.submit(new DependentService(countDownLatch));
        executorService.submit(new DependentService(countDownLatch));

        countDownLatch.await();

        System.out.println("All Dependent services initialized");
        executorService.shutdown();

    }
}
