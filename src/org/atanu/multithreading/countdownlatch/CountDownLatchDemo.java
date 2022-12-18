package org.atanu.multithreading.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//CountDownLatch is a synchronization primitive that comes with the java.util.concurrent package.
// It can be used to block a single or multiple threads while other threads complete their operations.
//
//A CountDownLatch object is initialized with the number of tasks/threads it is required to wait for.
// Multiple threads can block and wait for the CountDownLatch object to reach zero by invoking await().
// Every time a thread finishes its work, the thread invokes countDown() which decrements the counter by 1.
// Once the count reaches zero, threads waiting on the await() method are notified and resume execution.

//https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/gkw5QKGDwJZ
public class CountDownLatchDemo {

    private static class Worker implements Runnable{

        private CountDownLatch countDownLatch;

        public Worker(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("Worker " +Thread.currentThread().getName()+" started");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Each thread calls countDown() method on task completion.
            System.out.println("Worker  "+Thread.currentThread().getName()+" finished");
            countDownLatch.countDown();
        }
    }

    private static class Master implements Runnable{

        private CountDownLatch countDownLatch;

        public Master(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {

            try {
                //Waiting for Workers to be finished
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() +"Master executing once all the worker has Finished ");

        }
    }

    public static void main(String[] args) {

        int NoOfWorkers = 5;
        CountDownLatch countDownLatch = new CountDownLatch(NoOfWorkers);

        //Start Master Thread, But it has to wait for all the worker threads to be completed
        new Thread(new Master(countDownLatch)).start();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //Starting Workers
        for(int i = 0; i < NoOfWorkers; i++){
            executorService.submit(new Worker(countDownLatch));
        }
        //Shut Down the Pool
        executorService.shutdown();
    }
}
