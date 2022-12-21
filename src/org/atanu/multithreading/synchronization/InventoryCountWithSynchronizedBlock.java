package org.atanu.multithreading.synchronization;

//https://www.udemy.com/course/java-multithreading-concurrency-performance-optimization/learn/lecture/11200008#overview
//Threads are synchronized with lock Object
public class InventoryCountWithSynchronizedBlock {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter(); //shared Object
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }

    public static class DecrementingThread extends Thread {

        private InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    public static class IncrementingThread extends Thread {

        private InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }

    private static class InventoryCounter {
        private int items = 0;
        final Object lock = new Object();

        public void increment() {
            synchronized (this.lock){
                items++;
            }
        }

        public  void decrement() {
            synchronized (lock){ //this.lock and lock anything is fine
                items--;
            }
        }

        public int getItems() {
            return items;
        }
    }
}
