package org.atanu.multithreading;

import java.util.Random;

//https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/m2G48X18NDO
public class DemoThreadUnsafe {

    public static void main(String[] args) throws InterruptedException {

        // create object of unsafe counter
        BadCounter badCounter = new BadCounter();

        // setup thread1 to increment the badCounter 100 times
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    badCounter.increment();
                    sleepRandomlyForLessThan10Secs();
                }
            }
        });

        // setup thread2 to decrement the badCounter 100 times
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    badCounter.decrement();
                    sleepRandomlyForLessThan10Secs();
                }
            }
        });

        // run both threads
        threadA.start();
        threadB.start();

        // wait for t1 and t2 to complete.
        threadA.join();
        threadB.join();

        // print final value of counter
        badCounter.printFinalCounterValue();

    }

    //randomly sleep our threads
    public static void sleepRandomlyForLessThan10Secs() {
        try {
            Thread.sleep(new Random().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class BadCounter{

    int counter = 0;

    public void increment(){
        counter++;
    }

    public void decrement(){
        counter--;
    }

    public void printFinalCounterValue(){
        System.out.println("counter is: " + counter);
    }
}
