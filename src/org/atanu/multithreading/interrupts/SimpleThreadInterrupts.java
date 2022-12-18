package org.atanu.multithreading.interrupts;

//https://www.youtube.com/watch?v=-7ZB-jpaPPo&t=393s
//https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/RM5jZn2EzYY
//Note that there are two methods to check for the interrupt status of a thread. One is the static method Thread.interrupted() and the other is Thread.currentThread().isInterrupted().
//The important difference between the two is that the static method would return the interrupt status and also clear it at the same time.
public class SimpleThreadInterrupts {

    public static void main(String[] args){
        Thread longRunningThread = new Thread(new LongRunningTask());
        longRunningThread.start();
        longRunningThread.interrupt();

    }

    private static class LongRunningTask implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(20000);
                System.out.println("Done with processing");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().isInterrupted());
                System.out.println("I am interrupted while doing the task");
            }
        }
    }
}
