package org.atanu.multithreading.orderedprint;

// https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/B8Yz93o3w4Y
// Suppose there are three threads t1, t2 and t3. t1 prints First, t2 prints Second and t3 prints Third
public class OrderPrint {

    static class Printer{
        int count;

        Printer(){
            count = 1;
        }

        public void printFirst(){
            synchronized (this){
                System.out.println("First");
                count++;
                this.notifyAll();
            }
        }

        public void printSecond() throws InterruptedException {
            synchronized (this){
                while (count != 2){
                    this.wait();
                }
                System.out.println("Second");
                count++;
                this.notifyAll();
            }
        }

        public void printThird() throws InterruptedException {
            synchronized (this){
                while (count != 3){
                    this.wait();
                }
                System.out.println("Third");
                count++;
            }
        }
    }

    static class PrinterThread extends Thread{

        private Printer printer;
        private String methodType;

        PrinterThread(Printer printer, String methodType){
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

        Printer printer = new Printer();
        PrinterThread first = new PrinterThread(printer, "first");
        PrinterThread second = new PrinterThread(printer, "second");
        PrinterThread third = new PrinterThread(printer, "third");

        third.start();
        second.start();
        first.start();

    }
}
