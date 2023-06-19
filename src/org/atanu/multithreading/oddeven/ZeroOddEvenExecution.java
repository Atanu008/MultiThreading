package org.atanu.multithreading.oddeven;

import java.util.concurrent.Semaphore;

// https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/g2yrV93x6M9

// Suppose we are given a number n based on which a program creates the series 010203...0n.
// There are three threads t1, t2 and t3 which print a specific type of number from the series.
// t1 only prints zeros, t2 prints odd numbers and t3 prints even numbers from the series

// Our solution makes use of three semaphores;
// zeroSem for printing zeros, oddSem for printing odd numbers and evenSem for printing even numbers

// For oddSem and evenSem, all acquire()calls will be blocked initially as they are initialized with 0.
// For zeroSem, the first acquire() call will succeed as it is initialized with 1

// PrintZero() begins with a loop iterating from 0 till n(exclusive).
// The semaphore zeroSem is acquired and '0' is printed. A very significant line in this method is line 6 in the loop.
// The modulus operator (%) gives the remainder of a division by the value following it.
// In our case, the current value is divided by 2 to determine if i is even or odd.
// If i is odd, then it means we just printed an odd number
// and the next number in the sequence will be an even number so,evenSem is released.
// In the same way if i is even then oddSem is released for printing the next odd number in the sequence.

public class ZeroOddEvenExecution {

    static class ZeroOddEven{

        int n;
        Semaphore zeroSemaphore;
        Semaphore oddSemaphore;
        Semaphore evenSemaphore;

        ZeroOddEven(int n){
            this.n = n;
            zeroSemaphore = new Semaphore(1); // This is one as zero has to start first
            oddSemaphore = new Semaphore(0);
            evenSemaphore = new Semaphore(0);
        }

        public void zero(){

            for(int i = 0; i < n; i++){
                try {
                    zeroSemaphore.acquire();
                    System.out.print(0); // Print Zero
                    // release oddSem if i is even or else release evenSem if i is odd
                    if (i % 2 == 0) {
                        oddSemaphore.release();
                    } else {
                        evenSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        public void odd(){
            for (int i = 1; i <= n; i+=2){
                try {
                    oddSemaphore.acquire();
                    System.out.print(i); // Print Odd
                    zeroSemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void even(){
            for (int i = 2; i <= n; i+=2){
                try {
                    evenSemaphore.acquire();
                    System.out.print(i); // Print Even
                    zeroSemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ZeroOddEven zeroOddEven = new ZeroOddEven(5);
        Thread zeroThread = new Thread(() -> zeroOddEven.zero());
        Thread oddThread = new Thread(() -> zeroOddEven.odd());
        Thread evenThread = new Thread(() -> zeroOddEven.even());

        zeroThread.start();
        oddThread.start();
        evenThread.start();

    }
}
