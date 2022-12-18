package org.atanu.multithreading.barrier;

import java.util.Random;
import java.util.concurrent.*;

//https://www.youtube.com/watch?v=J3QZ5gfCtAg&t=39s
public class CyclicBarrierDoTaskInCycle {

    private static class Task implements Runnable{

        CyclicBarrier barrier;

        public Task(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(new Random().nextInt(3000));
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                //Once All the Thread reaches the no of parties in Cyclcic barrier then they will proceed
                // and again wait at the barrier is this in while true
                System.out.println(Thread.currentThread().getName() +": Lets Do it as all the Threads has Arrived ");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(new Task(cyclicBarrier));
        executorService.submit(new Task(cyclicBarrier));
        executorService.submit(new Task(cyclicBarrier));

        Thread.sleep(1000);
    }
}
