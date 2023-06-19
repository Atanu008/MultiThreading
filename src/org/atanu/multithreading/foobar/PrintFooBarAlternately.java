package org.atanu.multithreading.foobar;

// https://leetcode.com/problems/print-foobar-alternately/description/
// Leetcode 1115

//Suppose you are given the following code:
//
//class FooBar {
//  public void foo() {
//    for (int i = 0; i < n; i++) {
//      print("foo");
//    }
//  }
//
//  public void bar() {
//    for (int i = 0; i < n; i++) {
//      print("bar");
//    }
//  }
//}
//The same instance of FooBar will be passed to two different threads:
//
//thread A will call foo(), while
//thread B will call bar().
//Modify the given program to output "foobar" n times.

public class PrintFooBarAlternately {

    class FooBar {
        private int n;
        int flag = 0;  //flag 0->foo to be print  1->foo has been printed
        public FooBar(int n) {
            this.n = n;
        }

        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                synchronized(this){
                    while(flag == 1){
                        this.wait();
                    }
                    // printFoo.run() outputs "foo". Do not change or remove this line.
                    printFoo.run();
                    flag = 1;
                    this.notify();

                }

            }
        }

        public void bar(Runnable printBar) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                synchronized(this){
                    while(flag == 0){
                        this.wait();
                    }
                    // printBar.run() outputs "bar". Do not change or remove this line.
                    printBar.run();
                    flag = 0;
                    this.notify();
                }

            }
        }
    }
}

