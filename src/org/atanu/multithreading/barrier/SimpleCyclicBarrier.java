package org.atanu.multithreading.barrier;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/q2NLpkzMDNG

//CyclicBarrier is a synchronization mechanism introduced in JDK 5 in the java.util.concurrent package.
// It allows multiple threads to wait for each other at a common point (barrier) before continuing execution.
// The threads wait for each other by calling the await() method on the CyclicBarrier.
// All threads that wait for each other to reach barrier are called parties.
// You can read-up on designing and implementing a barrier for an interview question here.
//
//CyclicBarrier is initialized with an integer that denotes the number of threads that need to call the await() method on the barrier.
// Second argument in CyclicBarrier’s constructor is a Runnable instance that includes the action to be executed once the last thread arrives.
//
//The most useful property of CyclicBarrier is that it can be reset to its initial state by calling the reset() method.
// It can be reused after all the threads have been released.
//
//Lets take an example where CyclicBarrier is initialized with 3 worker threads that will have to cross the barrier.
// All the threads need to call the await() method. Once all the threads have reached the barrier,
//it gets broken and each thread starts its execution from that point onwards.
public class SimpleCyclicBarrier {

    //The Task
    private static class Task implements Runnable{

        private CyclicBarrier barrier;

        public Task(CyclicBarrier barrier){
            this.barrier = barrier;
        }

        public void run(){

            System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
            try {
                barrier.await();
                //printing after crossing the barrier
                System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ActionWhenBarrierTipped implements Runnable{
        @Override
        public void run() {
            System.out.println("All parties have arrived at the barrier, lets continue execution.");
            System.out.println(Thread.currentThread().getName() + " is executing this algorithm");
        }
    }

    public static void main(String[] args) {

        ActionWhenBarrierTipped action = new ActionWhenBarrierTipped();
        int parties = 5;
        CyclicBarrier barrier = new CyclicBarrier(parties, action);

        for(int i = 0; i < parties; i++){
            new Thread(new Task(barrier)).start();
        }
    }
}
