package org.atanu.multithreading.orderedprint;

// https://leetcode.com/problems/print-in-order/description/
// Leetcode 1114

// Suppose we have a class:
//
//public class Foo {
//  public void first() { print("first"); }
//  public void second() { print("second"); }
//  public void third() { print("third"); }
//}
//The same instance of Foo will be passed to three different threads. Thread A will call first(), thread B will call second(), and thread C will call third(). Design a mechanism and modify the program to ensure that second() is executed after first(), and third() is executed after second().
//
//Note:
//
//We do not know how the threads will be scheduled in the operating system, even though the numbers in the input seem to imply the ordering. The input format you see is mainly to ensure our tests' comprehensiveness.

public class PrintInOrder {

    private int count;
    public PrintInOrder() {
        count = 1;
    }

    public void first(Runnable printFirst) throws InterruptedException {

        synchronized(this){
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            count++;
            this.notifyAll();
        }

    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized(this){
            while(count != 2){
                this.wait();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            count++;
            this.notifyAll();
        }

    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized(this){
            while(count != 3){
                this.wait();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
            count++;
        }

    }
}
