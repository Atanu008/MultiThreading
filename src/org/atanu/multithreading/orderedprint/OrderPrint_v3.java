package org.atanu.multithreading.orderedprint;

import java.util.concurrent.CountDownLatch;

// https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/B8Yz93o3w4Y
// Suppose there are three threads t1, t2 and t3. t1 prints First, t2 prints Second and t3 prints Third


// using CountDownLatch

// The second solution includes the use of CountDownLatch; a synchronization utility used to achieve concurrency.
// It manages multithreading where a certain sequence of operations or tasks is required.
// Everytime a thread finishes its work, countdown() is invoked, decrementing the counter by 1.
// Once this count reaches zero, await() is notified
// and control is given back to the main thread that has been waiting for others to finish.

// The basic structure of the class OrderedPrintingis the same as presented in solution 1
// with the only difference of using countdownlatch instead of volatile variable.
// We have 2 countdownlatch variables that get initialized with 1 each.

// In printFirst() method, latch1 decrements and reaches 0, waking up the waiting threads consequently.
// In printSecond(), if latch1 is free (reached 0), then the printing is done and latch2 is decremented.
// Similarly in the third method printThird(),latch2 is checked and printing is done.
// The latches here act like switches/gates that get closed and opened for particular actions to pass.

public class OrderPrint_v3 {

    static class Printer{

        private CountDownLatch firstLatch;
        private CountDownLatch secondLatch;

        Printer(){
            firstLatch = new CountDownLatch(1);
            secondLatch = new CountDownLatch(1);
        }

        public void printFirst(){

            System.out.println("First");
            firstLatch.countDown();
        }

        public void printSecond() throws InterruptedException {

            firstLatch.await(); // Wait for First to complete. it will resume when firstLatch counts to zero
            System.out.println("Second");
            secondLatch.await();

        }

        public void printThird() throws InterruptedException {

            secondLatch.await(); // Wait for Second to complete. it will resume when secondLatch counts to zero
            System.out.println("Third");

        }
    }

    static class PrinterThread extends Thread{

        private OrderPrint.Printer printer;
        private String methodType;

        PrinterThread(OrderPrint.Printer printer, String methodType){
            this.printer = printer;
            this.methodType = methodType;

        }
        @Override
        public void run(){
            if(methodType.equals("first")){
                printer.printFirst();
            }else if(methodType.equals("second")){
                try {
                    printer.printSecond();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(methodType.equals("third")){
                try {
                    printer.printThird();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        OrderPrint.Printer printer = new OrderPrint.Printer();
        OrderPrint.PrinterThread first = new OrderPrint.PrinterThread(printer, "first");
        OrderPrint.PrinterThread second = new OrderPrint.PrinterThread(printer, "second");
        OrderPrint.PrinterThread third = new OrderPrint.PrinterThread(printer, "third");

        third.start();
        second.start();
        first.start();

    }
}
