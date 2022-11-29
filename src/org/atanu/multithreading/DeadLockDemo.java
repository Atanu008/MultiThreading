package org.atanu.multithreading;

//https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/JYzvKPXyA5P

//you'll see that the statements for acquiring locks: lock1 and lock2 print out
//but there's no progress after that and the execution times out.
//In this scenario, the deadlock occurs because the locks are being acquired in a nested fashion
public class DeadLockDemo {

    public static void main(String[] args) throws InterruptedException {

        DeadLock deadLock = new DeadLock();

        Runnable incrementer = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        deadLock.incrementCounter();
                        System.out.println("Incrementing " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable decrementer = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        deadLock.decrementCounter();
                        System.out.println("Decrementing " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread threadA = new Thread(incrementer);
        threadA.setName("Thread A - Incrementer");
        Thread threadB = new Thread(decrementer);
        threadB.setName("Thread B - Decrementer");

        threadA.start();
        // sleep to make sure thread 1 gets a chance to acquire lock1
        Thread.sleep(100);
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println("Done : " + deadLock.counter); // This will never be printed .Deadlock
    }


}

class DeadLock{

    public int counter;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    void incrementCounter() throws InterruptedException {
        synchronized (lock1){
            System.out.println(Thread.currentThread().getName() +" Acquired Lock1");
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName() +" Trying to Acquire Lock2");
            synchronized (lock2){
                counter++;
            }
        }
    }

    void decrementCounter() throws InterruptedException {
        synchronized (lock2){
            System.out.println(Thread.currentThread().getName() +" Acquired Lock2");
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName() +" Trying to Acquire Lock1");
            synchronized (lock1){
                counter++;
            }
        }
    }
}
