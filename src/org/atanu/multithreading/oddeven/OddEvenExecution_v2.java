package org.atanu.multithreading.oddeven;

import java.util.concurrent.Semaphore;

// A semaphore controls access to a shared resource through the use of a counter.
// If the counter is greater than zero, then access is allowed. If it is zero, then access is denied.

// We create two threads, an odd thread, and an even thread.
// The odd thread would print the odd numbers starting from 1,
// and the even thread will print the even numbers starting from 2.

// Important:
// Both the threads have an object of the SharedPrinter class.
// The SharedPrinter class will have two semaphores, semOdd and semEven which will have 1 and 0 permits to start with.
// This will ensure that odd number gets printed first.

// We have two methods printEvenNum() and printOddNum().
// The odd thread calls the printOddNum() method and the even thread calls the printEvenNum() method.
//
// To print an odd number, the acquire() method is called on semOdd, and since the initial permit is 1,
// it acquires the access successfully, prints the odd number and calls release() on semEven.

// Calling release() will increment the permit by 1 for semEven,
// and the even thread can then successfully acquire the access and print the even number.

public class OddEvenExecution_v2 {

    static class OddEven{

        int n;
        Semaphore oddSemaPhore;
        Semaphore evenSemaPhore;

        OddEven(int n){
            this.n = n;
            oddSemaPhore = new Semaphore(1);
            evenSemaPhore = new Semaphore(0);

        }
        public void odd(){
            //Only print Odd, start with 1 and in every iteration +2, so 1, 3, 5, 7..
            for(int i = 1; i <= n; i+=2){
                try {
                    oddSemaPhore.acquire();
                    System.out.println("Semaphore Odd "+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    evenSemaPhore.release(); // Release even as it will increase the even permit count
                }
            }
        }

        public void even(){
            //Only print Even, start with 2 and in every iteration +2, so 2, 4, 6, 8..
            for(int i = 2; i <= n; i+=2){
                try {
                    evenSemaPhore.acquire();
                    System.out.println("Semaphore even "+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    oddSemaPhore.release(); // Release Odd as it will increase the Odd permit count
                }
            }
        }
    }

    public static void main(String[] args) {
        int n = 9;
        OddEven oddEven = new OddEven(9);
        Thread oddThread = new Thread(() -> oddEven.odd());
        Thread evenThread = new Thread(() -> oddEven.even());

        oddThread.start();
        evenThread.start();
    }
}
