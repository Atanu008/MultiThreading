package org.atanu.multithreading.orderedprint;

// https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/B8Yz93o3w4Y
// Suppose there are three threads t1, t2 and t3. t1 prints First, t2 prints Second and t3 prints Third

// Here instead of one Counter using two boolean
// Second Thread will wait until first is complete
// Third will wait wait until second is complete
public class OrderPrint_v2 {

    static class Printer{

        private boolean firstDone;
        private boolean secnondDone;

        Printer(){
            firstDone = false;
            secnondDone = false;
        }

        public void printFirst(){
            synchronized (this){
                System.out.println("First");
                firstDone = true;
                this.notifyAll();
            }
        }

        public void printSecond() throws InterruptedException {
            synchronized (this){
                while (!firstDone){
                    this.wait();
                }
                System.out.println("Second");
                secnondDone = true;
                this.notifyAll();
            }
        }

        public void printThird() throws InterruptedException {
            synchronized (this){
                while (!secnondDone){
                    this.wait();
                }
                System.out.println("Third");
            }
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
