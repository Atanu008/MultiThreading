package org.atanu.multithreading.basics;

public class ThreadCreation {
    public static void main(String[] args) {

        Thread runnableThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Code that will run in  a new thread
                System.out.println("we are now in thread "+Thread.currentThread().getName());
                System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
            }
        });
        runnableThread.setName("New Runnable Worker Thread");
        runnableThread.setPriority(Thread.MAX_PRIORITY);

        MyThread extendedThread = new MyThread("Extented Thread Worker");
        extendedThread.setPriority(Thread.NORM_PRIORITY);

        System.out.println("We are in thread: " + Thread.currentThread().getName()+ " before starting a new thread");
        runnableThread.start();
        extendedThread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName()+ " after starting a new thread");
    }

    private static class MyThread extends Thread{

        public MyThread(String name){
            this.setName(name);
        }

        @Override
        public void run() {
            System.out.println("we are now in thread "+this.getName());
            System.out.println("Current thread priority is " + this.getPriority());
        }
    }
}
