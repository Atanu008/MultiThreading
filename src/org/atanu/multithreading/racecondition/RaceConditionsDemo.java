package org.atanu.multithreading.racecondition;

import java.util.concurrent.locks.ReentrantLock;

//This works . giving same output everytime . Should not be the case as the addition and multiplication order is changing
// Need to check . May be Non static locks
public class RaceConditionsDemo {


    public static void main(String[] args) throws InterruptedException {

        Bag shoppingBag = new Bag();
        Thread[] shoppers = new Thread[10];

        for (int i = 0; i < shoppers.length / 2; i++) {

            Thread atanuThread = new Thread(new Shopper(shoppingBag));
            atanuThread.setName("Atanu-"+i);
            shoppers[2 * i] = atanuThread;

            Thread rijuThread = new Thread(new Shopper(shoppingBag));
            rijuThread.setName("Riju-"+i);
            shoppers[2 * i + 1] = rijuThread;
        }
        for (Thread s : shoppers) {
            s.start();
        }

        for (Thread s : shoppers) {
            s.join();
        }

        System.out.println("We need to buy " + shoppingBag.bagsOfChips + " bags of chips.");

    }

    //Shopper Threads
    private static class Shopper implements Runnable {

        private Bag bag;

        public Shopper(Bag bag) {
            this.bag = bag;

        }

        @Override
        public void run() {
            String currentThreadName = Thread.currentThread().getName();
            if (currentThreadName.contains("Atanu")) {
                bag.addChips(6);
                System.out.println(currentThreadName + " ADDED three bags of chips.");
            } else {
                bag.multiplyChips(36);
                System.out.println(currentThreadName + " DOUBLED the bags of chips.");
            }
        }
    }

    //Bag is the shared object , that will be updated by Shopper threads
    private static class Bag {

        public int bagsOfChips;
        private static ReentrantLock lock = new ReentrantLock();

        public Bag() {
            bagsOfChips = 1;
        }

        public void addChips(int noOfChips) {
            try {
                lock.lock(); // Acquire the Lock
                System.out.println("Chips before adding "+ bagsOfChips);
                bagsOfChips += noOfChips; // Critical section. Add Chips
                System.out.println("Chips after adding "+ bagsOfChips);
            } finally {
                lock.unlock();
            }
        }

        public void multiplyChips(int noOfChips) {
            try {
                lock.lock(); // Acquire the Lock
                System.out.println("Chips before multiplying "+ bagsOfChips);
                bagsOfChips *= noOfChips; // Critical section. Multiply Chips
                System.out.println("Chips after multiplying "+ bagsOfChips);
            } finally {
                lock.unlock();
            }
        }
    }
}
