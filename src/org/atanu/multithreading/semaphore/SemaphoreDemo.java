package org.atanu.multithreading.semaphore;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Semaphore.html#acquire()
//https://www.youtube.com/watch?v=shH38znT_sQ
public class SemaphoreDemo {

    final static private int PERMIT = 3;
    final static private int THREAD_POOL_SIZE = 50;
    final static private int RANGE = 500;

    public static void main(String[] args) throws InterruptedException {

        Semaphore semaphore = new Semaphore(PERMIT);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        Task task = new Task(semaphore);

        IntStream.range(0, 500).forEach(i -> executorService.execute(task));

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }

    //Runnable dont allow checked exception to be thrown , and InterruptedException is Checked Exception
    // So We need try catch block for InterruptedException
    // Another would be to use Callable
    //Check Interrupts : https://www.youtube.com/watch?v=-7ZB-jpaPPo&list=PLhfHPmPYPPRk6yMrcbfafFGSbE2EPK_A6&index=15
    static class Task implements Runnable{

        Semaphore semaphore;

        public Task(Semaphore semaphore){
            this.semaphore = semaphore;
        }

        @Override
        public void run() {

            try {
                semaphore.acquire(); // Only 3 Thread can acquire at a Time as PERMIT is 3
                System.out.println(Thread.currentThread().getName() +" Acquired Semaphore");
                Thread.sleep(5000); // simulation of doing some job/io stuffs

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                semaphore.release(); // Best Practise is To release Semaphore in Finally
            }

            System.out.println(Thread.currentThread().getName() +" Released Semaphore");
        }

        private long getRandomSleep() {
            return new Random().nextInt(200);
        }
    }
}
