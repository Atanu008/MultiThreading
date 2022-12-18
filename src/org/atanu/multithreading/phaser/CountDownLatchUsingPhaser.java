package org.atanu.multithreading.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class CountDownLatchUsingPhaser {

    public static void main(String[] args) {
        int noOfWorker = 3;
        Phaser phaser = new Phaser(noOfWorker);

        ExecutorService executorService = Executors.newFixedThreadPool(noOfWorker);
        executorService.submit(new DependentService(phaser));
        executorService.submit(new DependentService(phaser));

        phaser.arriveAndAwaitAdvance();

        System.out.println("All Dependent services initialized");
        executorService.shutdown();
    }

    private static class DependentService implements Runnable {
        private Phaser phaser;

        public DependentService(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " performed DependentService ");
            phaser.arriveAndAwaitAdvance();
        }
    }
}
