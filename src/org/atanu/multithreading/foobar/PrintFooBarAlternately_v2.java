package org.atanu.multithreading.foobar;

// https://leetcode.com/problems/print-foobar-alternately/description/
// Leetcode 1115

// Using Locks

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintFooBarAlternately_v2 {

    class FooBar {
        private int n;
        int flag = 0;
        ReentrantLock reentrantLock;
        Condition fooDone;
        Condition barDone;

        public FooBar(int n) {
            this.n = n;
            reentrantLock = new ReentrantLock();
            fooDone = reentrantLock.newCondition();
            barDone = reentrantLock.newCondition();
        }

        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                try{
                    reentrantLock.lock();
                    while(flag == 1){
                        barDone.await();
                    }
                    // printFoo.run() outputs "foo". Do not change or remove this line.
                    printFoo.run();
                    flag = 1;
                    fooDone.signalAll();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    reentrantLock.unlock();
                }

            }
        }

        public void bar(Runnable printBar) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                try{
                    reentrantLock.lock();
                    while(flag == 0){
                        fooDone.await();
                    }
                    // printBar.run() outputs "bar". Do not change or remove this line.
                    printBar.run();
                    flag = 0;
                    barDone.signalAll();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    reentrantLock.unlock();
                }

            }
        }
    }
}
